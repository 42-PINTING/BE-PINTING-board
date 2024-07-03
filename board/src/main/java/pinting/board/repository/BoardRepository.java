package pinting.board.repository;

import pinting.board.domain.Post;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    void save(Post post);
    void deleteById(Long id);
    Optional<Post> findOneById(Long id);
    List<Post> findByAuthor(Long author_id);
    List<Post> findAll();
    List<Post> getRandomPosts(int n);
    List<Post> findPostsByKeyword(String keyword);
    List<Post> findPostsByTags(List<String> tags);
}
