package pinting.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.config.AppConfig;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.domain.QPost;
import pinting.board.domain.Tag;
import pinting.board.service.BoardService;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pinting.board.domain.QPost.*;

@Transactional
@SpringBootTest(classes = AppConfig.class)
public class JPABoardRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        PostForm form1 = createForm(1L);
        Post postA = new Post(form1);
        PostForm form2 = createForm(2L);
        Post postB = new Post(form2);
        PostForm form3 = createForm(3L);
        Post postC = new Post(form3);
        PostForm form4 = createForm(4L);
        Post postD = new Post(form4);
        em.persist(postA);
        em.persist(postB);
        em.persist(postC);
        em.persist(postD);

        Tag diary1 = new Tag("diary");
        Tag diary2 = new Tag("diary");
        Tag feeling1 = new Tag("feeling");
        Tag feeling2 = new Tag("feeling");
        em.persist(diary1);
        em.persist(diary2);
        em.persist(feeling1);
        em.persist(feeling2);

        diary1.changePost(postA);
        diary2.changePost(postD);
        feeling1.changePost(postC);
        feeling2.changePost(postD);
    }

    public PostForm createForm(Long id) {
        return new PostForm(id, "title " + id, "img " + id, "content " + id, "PUBLIC", null);
    }

    public Post createPost(Long authorId, String title, String content) {
        PostForm form = createForm(authorId);
        form.setTitle(title);
        form.setContent(content);
        return new Post(form);
    }

    @Test
    public void 게시물_저장() {
        PostForm form = createForm(42L);
        boardRepository.save(new Post(form));

        Post findPost = queryFactory
                .selectFrom(post)
                .where(post.authorId.eq(42L))
                .fetchOne();

        assertThat(findPost).isNotNull();
    }


    @Test
    public void 게시물_삭제() {
        boardRepository.deleteById(1L);

        Post findPost = em.find(Post.class, 1L);

        assertThat(findPost).isNull();
    }

    @Test
    public void ID로_조회() {
        Post addPost = createPost(42L, "title", "content");
        em.persist(addPost);

        Optional<Post> findPost = boardRepository.findOneById(addPost.getId());

        System.out.println("findPost = " + findPost);
        assertThat(findPost).isPresent();
    }

    @Test
    public void 작가_게시물_조회() {
        Post post1 = createPost(42L, "42's title 1", "42's content 1");
        Post post2 = createPost(42L, "42's title 2", "42's content 2");
        Post post3 = createPost(42L, "42's title 3", "42's content 3");
        em.persist(post1);
        em.persist(post2);
        em.persist(post3);

        List<Post> results = boardRepository.findByAuthor(42L);

        assertThat(results.size()).isEqualTo(3);
        assertThat(results.get(0).getTitle()).isEqualTo("42's title 1");
    }

    @Test
    public void 존재하지_않는_키워드로_검색() {
        List<Post> results = boardRepository.searchPostsByKeyword("없음");

        assertThat(results.size()).isEqualTo(0);
    }

    @Test
    public void 존재하는_키워드로_검색() {
        List<Post> results = boardRepository.searchPostsByKeyword("title");

        assertThat(results.size()).isEqualTo(4);
    }

    @Test
    public void 태그_검색() {
        List<String> tags = new ArrayList<>();
        tags.add("diary");
        List<Post> results = boardRepository.searchPostsByTags(tags);

        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    public void 빈_태그로_검색() {
        List<String> tags = new ArrayList<>();
        List<Post> results = boardRepository.searchPostsByTags(tags);

        assertThat(results.size()).isEqualTo(0);
    }

    @Test
    public void 없는_태그_검색() {
        List<String> tags = new ArrayList<>();
        tags.add("does not exist");
        List<Post> results = boardRepository.searchPostsByTags(tags);

        for (Post result : results) {
            System.out.println("result = " + result);
        }

        assertThat(results.size()).isEqualTo(0);
    }
}