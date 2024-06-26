package pinting.board.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pinting.board.domain.Post;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    void save(Post post);
    void deleteById(Long id);
    Optional<Post> findOneById(Long id);
    Optional<Post> findOneByTitle(String title);
    List<Post> findByAuthor(Long author_id);
    List<Post> findAll();
}
