package pinting.board.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.domain.Post;
import pinting.board.domain.Tag;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JPATagRepository implements TagRepository {

    private final EntityManager em;

    @Override
    @Transactional
    public void save(Tag tag) {
        em.persist(tag);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Tag existing = em.find(Tag.class, id);
        if (existing != null) {
            this.em.remove(this.em.contains(existing) ? existing : this.em.merge(existing));
        }
    }

    @Override
    public Optional<Tag> findOneById(Long id) {
        return Optional.ofNullable(em.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> findOneByName(String name) {
        return Optional.ofNullable(em.createQuery("select t from Tag t where t.name = :name", Tag.class)
                .setParameter("name", name).getSingleResult());
    }

    @Override
    public List<Tag> findOneByPostId(Long postId) {
        return em.createQuery("select t from Tag as t where t.post.id = :post", Tag.class)
                .setParameter("post", postId)
                .getResultList();
    }

    @Override
    public List<Tag> findAll() {
        return em.createQuery("select t from Tag t", Tag.class).getResultList();
    }

}
