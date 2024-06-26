package pinting.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.domain.Post;
import pinting.board.repository.BoardRepository;

import javax.sound.sampled.Port;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
   private final BoardRepository boardRepository;

   public Long createPost(Post post) {
      boardRepository.save(post);
      return post.getId();
   }

   public Optional<Post> readOnePostById(Long id) {
      return Optional.ofNullable(boardRepository.findOneById(id).get());
   }

   public void updatePost(Long post, PostUpdateDto dto) {

   }

   /**
    * TODO: public, 검색 옵션 반영
    */
   public List<Post> getMainPagePost() {
      List<Post> trimmed = boardRepository.findAll();
      return boardRepository.findAll();
   }

   public Long updatePost(Long id, Post post) {
      boardRepository.save(post);
      return id;
   }

   public void deletePost(Long id) {
      boardRepository.deleteById(id);
   }

   // TODO: 검색 기능 추가
}
