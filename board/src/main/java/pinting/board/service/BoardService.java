package pinting.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.domain.Post;
import pinting.board.domain.Tag;
import pinting.board.repository.BoardRepository;
import pinting.board.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
   private final BoardRepository boardRepository;
   private final TagRepository tagRepository;

    public Long createPost(Post post) {
      boardRepository.save(post);
      // Q: 하나씩 날리는 것이 맞을까요?
      for (Tag tag : post.getTags()) {
         tagRepository.save(tag);
      }
      return post.getId();
   }

   @Transactional(readOnly = true)
   public Optional<Post> readOnePostById(Long id) {
      return boardRepository.findOneById(id);
   }

   public Optional<Post> updatePost(Long postId, PostUpdateDto dto) {
      Optional<Post> findPost = boardRepository.findOneById(postId);
      if (findPost.isEmpty()) {
         return Optional.empty();
      }
      /**
       * 기존의 태그 리스트를 업데이트 하려는 태그 이름으로 검색하여 없는 것은 추가, 있는 것은 패스,
       * 업데이트 하려는 태그 리스트를 기존의 태그 리스트로 검색하여 없는 것은 삭제, 있는 것은 패스
       * 비효율적으로 보이기 때문에, 리팩토링을 해야 한다. issue number: #4
       */
      List<Tag> originTags = tagRepository.findAllByPostId(postId);
      for (Tag originTag : originTags) {
         if (!dto.getTags().contains(originTag.name)) {
            tagRepository.deleteById(originTag.getId());
         }
      }
      for (String updateTagName : dto.getTags()) {
         if (!originTags.stream().map(Tag::getName).toList().contains(updateTagName)) {
            Tag updateTag = new Tag(updateTagName, findPost.get());
            tagRepository.save(updateTag);
         }
      }
      findPost.get().update(dto);
      return findPost;
   }

   public void deletePost(Long id) {
      boardRepository.deleteById(id);
   }

   @Transactional(readOnly = true)
   public List<Post> getMainPagePost() {
       int MAIN_PAGE_POST_COUNT = 9;
       return boardRepository.getRandomPosts(MAIN_PAGE_POST_COUNT);
   }

   @Transactional(readOnly = true)
   public List<Post> searchPostByKeyword(String keyword) {
       return boardRepository.findPostsByKeyword(keyword);
   }

   @Transactional(readOnly = true)
   public List<Post> searchPostByTag(List<String> tags) {
       return boardRepository.findPostsByTags(tags);
   }

   @Transactional(readOnly = true)
   public List<Post> searchPostByAuthor(Long authorId) {
      return boardRepository.findByAuthor(authorId);
   }

   /**
    * like 증가
    * Todo: Like도 엔티티로 두어야 한다.
    * - 유저가 like한 게시물을 알 수 있어야 함.
    */
   public boolean likePost(Long postId) {
      Optional<Post> findPost = boardRepository.findOneById(postId);

      if (findPost.isEmpty()) {
         return false;
      }
      findPost.get().increaseLikeCount();
      return true;
   }

   /**
    * like 감소
    * 좋아요 취소만 있어야 한다.
    * 해당 기능은 좋아요를 누른 사람만 가능해야 하기 때문에 count 증감으로만 처리할 수 없다.
    */
//   public boolean dislikePost(Long postId) {
//      Optional<Post> findPost = boardRepository.findOneById(postId);
//
//      if (findPost.isEmpty()) {
//         return false;
//      }
//      findPost.get().decreaseLikeCount();
//      return true;
//   }

   /**
    * 게시물 숨김
    */
   public boolean hiddenPost(Long postId) {
      Optional<Post> findPost = boardRepository.findOneById(postId);
      if (findPost.isEmpty()) {
         return false;
      }
      findPost.get().hiddenPost();
      return true;
   }

   /**
    * 게시물 공개
    */
   public boolean publishPost(Long postId) {
      Optional<Post> findPost = boardRepository.findOneById(postId);
      if (findPost.isEmpty()) {
         return false;
      }
      findPost.get().publishPost();
      return true;
   }
}
