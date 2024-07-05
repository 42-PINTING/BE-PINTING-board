package pinting.board.repository;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Like;
import pinting.board.domain.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JPALikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private EntityManager em;

    private Post createPostWithAuthorId(Long authorId) {
        String title = "title " + authorId;
        String image = "image " + authorId;
        String content = "content " + authorId;
        String status = "PUBLIC";
        List<String> tags = new ArrayList<>();
        return new Post(new PostForm(authorId, title, image, content, tags));
    }

    private Like createLikeWithPost(Long memberId, Long authorId) {
        Post post = createPostWithAuthorId(authorId);
        em.persist(post);
        return new Like(post, memberId);
    }

    @Test
    void 좋아요_저장() {
        Like target = createLikeWithPost(1L, 42L);
        likeRepository.save(target);
        Long targetId = target.getId();

        em.flush();
        em.clear();

        Like findLike = em.find(Like.class, targetId);

        assertThat(findLike.getId()).isEqualTo(targetId);
    }

    @Test
    void 좋아요_삭제() {
        Like target = createLikeWithPost(1L, 42L);
        likeRepository.save(target);
        Long targetId = target.getId();

        em.flush();
        em.clear();

        likeRepository.deleteById(targetId);

        Like findLike = em.find(Like.class, targetId);

        assertThat(findLike).isNull();
    }

    @Test
    void 좋아요_ID로_검색() {
        Like target = createLikeWithPost(1L, 42L);
        likeRepository.save(target);
        Long targetId = target.getId();

        em.flush();
        em.clear();

        Optional<Like> findLike = likeRepository.findOneById(targetId);

        assertThat(findLike.isPresent()).isTrue();
        assertThat(findLike.get().getId()).isEqualTo(targetId);
    }

    @Test
    void 좋아요_존재하지_않는_ID로_검색() {
        Like target = createLikeWithPost(1L, 42L);
        likeRepository.save(target);
        Long targetId = target.getId();

        em.flush();
        em.clear();

        Optional<Like> findLike = likeRepository.findOneById(targetId + 42);

        assertThat(findLike.isEmpty()).isTrue();
    }

    @Test
    void 좋아요_postId로_검색() {
        Post targetPost = createPostWithAuthorId(42L);
        em.persist(targetPost);

        for (long i = 0; i < 42; i++) {
            likeRepository.save(new Like(targetPost, i));
        }

        em.flush();
        em.clear();

        List<Like> findLikes = likeRepository.findAllByPostId(targetPost.getId());
        assertThat(findLikes.size()).isEqualTo(42);
    }


    @Test
    void 좋아요_존재하지_않는_postId로_검색() {
        Post targetPost = createPostWithAuthorId(42L);
        em.persist(targetPost);

        for (long i = 0; i < 42; i++) {
            likeRepository.save(new Like(targetPost, i));
        }

        em.flush();
        em.clear();

        List<Like> findLikes = likeRepository.findAllByPostId(targetPost.getId() + 42);
        assertThat(findLikes).isEmpty();
    }

    @Test
    void MemberId로_검색() {
        Post targetPost = createPostWithAuthorId(42L);
        em.persist(targetPost);

        for (long i = 0; i < 42; i++) {
            likeRepository.save(new Like(targetPost, i));
        }

        em.flush();
        em.clear();

        List<Like> findLikes = likeRepository.findAllByMemberId(41L);
        assertThat(findLikes.size()).isEqualTo(1);
        assertThat(findLikes.get(0).getMemberId()).isEqualTo(41L);
    }

    @Test
    void MemberId로_여러_게시물_검색() {

        Long memberId = 1L;

        for (long i = 0; i < 42; i++) {
            Post targetPost = createPostWithAuthorId(i);
            em.persist(targetPost);
            if (i % 2 == 0) {
                likeRepository.save(new Like(targetPost, memberId));
            }
        }

        em.flush();
        em.clear();

        List<Like> findLikes = likeRepository.findAllByMemberId(memberId);
        assertThat(findLikes.size()).isEqualTo(21);
    }

    @Test
    void 멤버와_게시물ID로_검색() {
        Post targetPost = createPostWithAuthorId(42L);
        em.persist(targetPost);

        likeRepository.save(new Like(targetPost, 1L));
        /**
         * TODO: 다수의 중복된 Like는 서비스 단에서 막아주어야 하는가?
         */
//        for (long i = 0; i < 42; i++) {
//            likeRepository.save(new Like(targetPost, 1L));
//        }

        Optional<Like> findLike = likeRepository.findOneByMemberIdAndPostId(1L, targetPost.getId());
        assertThat(findLike.isPresent()).isTrue();
        assertThat(findLike.get().getMemberId()).isEqualTo(1L);

    }
}