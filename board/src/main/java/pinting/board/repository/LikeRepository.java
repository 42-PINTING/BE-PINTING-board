package pinting.board.repository;

import pinting.board.domain.Like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {
    void save(Like like);
    void deleteById(Long id);
    Optional<Like> findOneById(Long id);
    List<Like> findAllByPostId(Long postId);
    List<Like> findAllByMemberId(Long memberId);
    List<Like> findAll();
}
