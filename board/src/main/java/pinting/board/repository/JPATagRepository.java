package pinting.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.domain.Tag;

import java.util.List;
import java.util.Optional;

import static pinting.board.domain.QTag.tag;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JPATagRepository implements TagRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

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
    public List<Tag> findAllByName(String name) {
        return queryFactory
                .selectFrom(tag)
                .where(tag.name.eq(name))
                .fetch();
    }

    @Override
    public List<Tag> findAllByPostId(Long postId) {
        return queryFactory
                .selectFrom(tag)
                .where(tag.post.id.eq(postId))
                .fetch();
    }

    @Override
    public List<Tag> findAll() {
        return queryFactory
                .selectFrom(tag)
                .fetch();
    }

}
