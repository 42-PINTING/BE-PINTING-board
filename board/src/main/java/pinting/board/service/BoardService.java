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
      return boardRepository.findOneById(id);
   }

   public List<Post> getMainPagePost() {
      List<Post> trimmed = boardRepository.findAll();
      // 10개 선별
      return boardRepository.findAll();
   }

   // TODO: 검색 기능 추가
}
