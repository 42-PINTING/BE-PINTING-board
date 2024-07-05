package pinting.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.domain.Like;
import pinting.board.domain.Post;
import pinting.board.domain.Tag;
import pinting.board.repository.BoardRepository;
import pinting.board.repository.LikeRepository;
import pinting.board.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
   private final BoardRepository boardRepository;
   private final TagRepository tagRepository;
   private final LikeRepository likeRepository;

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
      List<String> targetTags = dto.getTags().stream().distinct().toList();
      List<Tag> originTags = tagRepository.findAllByPostId(postId);
      for (Tag originTag : originTags) {
         if (!targetTags.contains(originTag.name)) {
            tagRepository.deleteById(originTag.getId());
            // TODO: originTags에서도 지워주어야 하는지
         }
      }
      for (String updateTagName : targetTags) {
         if (!originTags.stream().map(Tag::getName).toList().contains(updateTagName)) {
            Tag updateTag = new Tag(updateTagName, findPost.get());
            tagRepository.save(updateTag);
            // TODO: originTags에도 추가를 해야 하는지
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
       List<String> tagsSet = tags.stream().distinct().toList();
       return boardRepository.findPostsByTags(tagsSet);
   }

   @Transactional(readOnly = true)
   public List<Post> searchPostByAuthor(Long authorId) {
      return boardRepository.findByAuthor(authorId);
   }

   /**
    * like 추가
    */
   public boolean likePost(Long memberId, Long postId) {
      Optional<Post> findPost = boardRepository.findOneById(postId);

      if (findPost.isEmpty()) {
         return false;
      }
      Post targetPost = findPost.get();
      if (targetPost.getLikes().stream().map(Like::getMemberId).toList().contains(memberId)) {
         return true;
      }
      likeRepository.save(new Like(targetPost, memberId));
      return true;
   }

   /**
    * like 취소
    */
   public boolean cancelLike(Long memberId, Long postId) {
      Optional<Like> findLike = likeRepository.findOneByMemberIdAndPostId(memberId, postId);
      if (findLike.isPresent()) {
         likeRepository.deleteById(findLike.get().getId());
         return true;
      }
      return false;
   }

   /**
    * Post like 개수 반환
    */
   public int getPostLikesCount(Long postId) {
      return likeRepository.findAllByPostId(postId).size();
   }

   /**
    * Post like 유저 id 목록 반환
    * Todo: id만 반환하기 때문에, 실제 유저는 Member 서비스에 직접 요청해야 한다.
    */
   public List<Long> getPostLikeMembers(Long postId) {
      return likeRepository.findAllByPostId(postId).stream().map(Like::getMemberId).toList();
   }

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
