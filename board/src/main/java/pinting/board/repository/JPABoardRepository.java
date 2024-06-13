package pinting.board.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.domain.Post;
 
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JPABoardRepository implements BoardRepository {

    private final EntityManager em;

    @Override
    public void save(Post post) {
        em.persist(post);
    }

    @Override
    public Optional<Post> findOneById(Long id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    @Override
    public Optional<Post> findOneByTitle(String title) {
        return Optional.ofNullable(em.createQuery("select p from Post p where p.title = :title", Post.class)
                .setParameter("title", title).getSingleResult());
    }

    @Override
    public List<Post> findByAuthor(Long author_id) {
        return em.createQuery("select p from Post p where p.author_id = :author_id", Post.class)
                .setParameter("author_id", author_id)
                .getResultList();
    }

    @Override
    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class).getResultList();
    }

}
