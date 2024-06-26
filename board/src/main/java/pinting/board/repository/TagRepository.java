package pinting.board.repository;

import pinting.board.domain.Post;
import pinting.board.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    void save(Tag tag);
    void deleteById(Long id);
    Optional<Tag> findOneById(Long id);
    Optional<Tag> findOneByName(String name);
    List<Tag> findOneByPostId(Long postId);
    List<Tag> findAll();
}
