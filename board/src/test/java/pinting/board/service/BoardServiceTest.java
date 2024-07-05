package pinting.board.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pinting.board.domain.QPost.*;
import static pinting.board.domain.QTag.*;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private EntityManager em;

    private Post createSamplePost(Long authorId, List<String> tags) {
        String title = "title " + authorId;
        String image = "image " + authorId;
        String content = "content " + authorId;
        String status = "PUBLIC";
        return new Post(new PostForm(authorId, title, image, content, tags));
    }

    private List<String> createTags(String ... strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }

    @Test
    public void 포스트_생성() {

        List<String> tag1 = createTags("test1", "test2");
        Long saveId = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        Post findPost = queryFactory
                .selectFrom(post)
                .join(post.tags, tag)
                .where(post.id.eq(saveId))
                .fetchOne();

        System.out.println("findPost = " + findPost);
        assertThat(findPost).isNotNull();
        assertThat(findPost.getId()).isEqualTo(saveId);
    }

    @Test
    public void 포스트_아이디로_검색() {
        List<String> tag1 = createTags("test1", "test2");
        Long saveId = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        Optional<Post> findPost = boardService.readOnePostById(saveId);

        System.out.println("findPost = " + findPost);
        assertThat(findPost.isPresent()).isTrue();
        assertThat(findPost.get().getId()).isEqualTo(saveId);
    }

    @Test
    public void 존재하지_않는_포스트_아이디로_검색() {
        List<String> tag1 = createTags("test1", "test2");
        Long saveId = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        Optional<Post> findPost = boardService.readOnePostById(saveId + 42);

        assertThat(findPost.isEmpty()).isTrue();
    }

    @Test
    public void 포스트_업데이트() {
        List<String> tag1 = createTags("test1", "test2");
        Long saveId = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        List<String> tag2 = createTags("update1", "update2");

        PostUpdateDto updateDto = new PostUpdateDto("update", "update", "update", tag2);
        boardService.updatePost(saveId, updateDto);

        em.flush();
        em.clear();

        Post findPost = em.find(Post.class, saveId);
        assertThat(findPost.getTitle()).isEqualTo("update");
    }

    @Test
    public void 포스트_삭제() {
        List<String> tag1 = createTags();
        Long saveId = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        boardService.deletePost(saveId);
        Optional<Post> findPost = boardService.readOnePostById(saveId);
        assertThat(findPost.isEmpty()).isTrue();
    }

    @Test
    public void 존재하지_않는_포스트_삭제() {
        List<String> tag1 = createTags();
        Long saveId = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        boardService.deletePost(saveId+42);
        Optional<Post> findPost = boardService.readOnePostById(saveId);
        assertThat(findPost.isPresent()).isTrue();
        assertThat(findPost.get().getId()).isEqualTo(saveId);
    }

    @Test
    public void 키워드_검색() {
        List<String> tag1 = createTags();
        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));
        Long saveId2 = boardService.createPost(createSamplePost(1L, tag1));
        Long saveId3 = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        List<Post> results = boardService.searchPostByKeyword("title");

        for (Post post : results) {
            System.out.println("post = " + post.getTitle());
        }

        assertThat(results.size()).isEqualTo(3);
        assertThat(results).extracting("id").contains(saveId1, saveId2, saveId3);
    }

    @Test
    public void 존재하지_않는_키워드_검색() {
        List<String> tag1 = createTags();
        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));
        Long saveId2 = boardService.createPost(createSamplePost(1L, tag1));
        Long saveId3 = boardService.createPost(createSamplePost(1L, tag1));

        em.flush();
        em.clear();

        List<Post> results = boardService.searchPostByKeyword("존재하지 않는 키워드");

        assertThat(results).isEmpty();
    }

    @Test
    public void 태그_검색() {
        List<String> tag1 = createTags("tag1");
        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));
        List<String> tag2 = createTags("tag1", "tag2");
        Long saveId2 = boardService.createPost(createSamplePost(1L, tag2));
        List<String> tag3 = createTags("tag1", "tag2", "tag3");
        Long saveId3 = boardService.createPost(createSamplePost(1L, tag3));

        em.flush();
        em.clear();

        List<String> searchingTags1 = createTags("tag1");
        List<Post> results1 = boardService.searchPostByTag(searchingTags1);

        assertThat(results1.size()).isEqualTo(3);

        List<String> searchingTags2 = createTags("tag2");
        List<Post> results2 = boardService.searchPostByTag(searchingTags2);

        assertThat(results2.size()).isEqualTo(2);

        List<String> searchingTags3 = createTags("tag3");
        List<Post> results3 = boardService.searchPostByTag(searchingTags3);

        assertThat(results3.size()).isEqualTo(1);

        List<String> searchingTags4 = createTags();
        List<Post> results4 = boardService.searchPostByTag(searchingTags4);

        assertThat(results4.size()).isEqualTo(0);
    }

    @Test
    void 작가_이름으로_검색() {
        List<String> tag1 = createTags();
        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));
        Long saveId2 = boardService.createPost(createSamplePost(1L, tag1));
        Long saveId3 = boardService.createPost(createSamplePost(2L, tag1));
        Long saveId4 = boardService.createPost(createSamplePost(1L, tag1));
        em.flush();
        em.clear();
        List<Post> results = boardService.searchPostByAuthor(1L);
        assertThat(results.size()).isEqualTo(3);
    }
//
//    @Test
//    void 좋아요_한_번() {
//        List<String> tag1 = createTags();
//        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));
//
//        boardService.likePost(saveId1);
//
//        em.flush();
//        em.clear();
//
//        Optional<Post> findPost = boardService.readOnePostById(saveId1);
//        assertThat(findPost.get().getLikeCount()).isEqualTo(1);
//    }
//
//    @Test
//    void 좋아요_여러_번() {
//        List<String> tag1 = createTags();
//        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));
//        for (int i = 0; i < 42; i++) {
//            boardService.likePost(saveId1);
//        }
//
//        em.flush();
//        em.clear();
//
//        Optional<Post> findPost = boardService.readOnePostById(saveId1);
//    }
    
    @Test
    void 게시물_숨김() {
        List<String> tag1 = createTags();
        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));

        boardService.hiddenPost(saveId1);
        
        em.flush();
        em.clear();

        Optional<Post> findPost = boardService.readOnePostById(saveId1);
        assertThat(findPost.get().getHiddenTime()).isNotNull();
    }

    @Test
    void 게시물_공개() {
        List<String> tag1 = createTags();
        Long saveId1 = boardService.createPost(createSamplePost(1L, tag1));

        boardService.hiddenPost(saveId1);

        Optional<Post> findPost = boardService.readOnePostById(saveId1);
        assertThat(findPost.get().getHiddenTime()).isNotNull();

        boardService.publishPost(saveId1);

        em.flush();
        em.clear();

        findPost = boardService.readOnePostById(saveId1);
        assertThat(findPost.get().getHiddenTime()).isNull();
    }

    @Test
    void 랜덤_게시물() {
        
    }
}