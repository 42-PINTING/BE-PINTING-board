package pinting.board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.config.AppConfig;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;

import java.util.List;

@Transactional
@SpringBootTest(classes = AppConfig.class)
public class JPABoardRepositoryTest {

    @Test
    public void saveTest() {

        ApplicationContext ac = new AnnotationConfigReactiveWebApplicationContext(AppConfig.class);
        JPABoardRepository repository = ac.getBean(JPABoardRepository.class);

        PostForm postForm = new PostForm();
        postForm.setImg("image");
        postForm.setTitle("title");
        postForm.setContent("content");
        postForm.setAuthorId(4242L);
        postForm.setStatus("PUBLIC");
        Post post = new Post(postForm);

        repository.save(post);
        Post findPost = repository.findOneById(post.getId()).get();
        Assertions.assertThat(post.getTitle()).isEqualTo(findPost.getTitle());
    }

    @Test
    public void findByAllTest() {

        ApplicationContext ac = new AnnotationConfigReactiveWebApplicationContext(AppConfig.class);
        JPABoardRepository repository = ac.getBean(JPABoardRepository.class);

        PostForm postForm = new PostForm();
        postForm.setImg("image_target1");
        postForm.setTitle("title_target1");
        postForm.setContent("content_target1");
        postForm.setAuthorId(110001L);
        postForm.setStatus("PUBLIC");
        Post post1 = new Post(postForm);

        postForm.setImg("image_target2");
        postForm.setTitle("title_target2");
        postForm.setContent("content_target2");
        postForm.setAuthorId(110002L);
        postForm.setStatus("PUBLIC");
        Post post2 = new Post(postForm);

        repository.save(post1);
        repository.save(post2);

        // When
        List<Post> findPosts = repository.findAll();

        // Then
        Assertions.assertThat(findPosts.size()).isEqualTo(2);
    }

    @Test
    public void findOneAuthorMultiplePostTest() {

        ApplicationContext ac = new AnnotationConfigReactiveWebApplicationContext(AppConfig.class);
        JPABoardRepository repository = ac.getBean(JPABoardRepository.class);

        Long authorId = 4242L;
        PostForm postForm = new PostForm();
        postForm.setImg("image_target1");
        postForm.setTitle("title_target1");
        postForm.setContent("content_target1");
        postForm.setAuthorId(authorId);
        postForm.setStatus("PUBLIC");
        Post post1 = new Post(postForm);

        postForm.setImg("image_target2");
        postForm.setTitle("title_target2");
        postForm.setContent("content_target2");
        postForm.setAuthorId(authorId);
        postForm.setStatus("PUBLIC");
        Post post2 = new Post(postForm);

        repository.save(post1);
        repository.save(post2);

        // When
        List<Post> oneAuthorPosts = repository.findByAuthor(authorId);

        // Then
        Assertions.assertThat(post1.getTitle()).isEqualTo(oneAuthorPosts.get(0).getTitle());
        Assertions.assertThat(post2.getTitle()).isEqualTo(oneAuthorPosts.get(1).getTitle());
        Assertions.assertThat(oneAuthorPosts.size()).isEqualTo(2);
    }

    @Test
    void findByTitleTest() {
        ApplicationContext ac = new AnnotationConfigReactiveWebApplicationContext(AppConfig.class);
        JPABoardRepository repository = ac.getBean(JPABoardRepository.class);

        Long authorId = 4242L;
        PostForm postForm = new PostForm();
        postForm.setImg("image_target1");
        postForm.setTitle("title_target1");
        postForm.setContent("content_target1");
        postForm.setAuthorId(authorId);
        postForm.setStatus("PUBLIC");
        Post post1 = new Post(postForm);

        postForm.setImg("image_target2");
        postForm.setTitle("title_target2");
        postForm.setContent("content_target2");
        postForm.setAuthorId(authorId);
        postForm.setStatus("PUBLIC");
        Post post2 = new Post(postForm);

        repository.save(post1);
        repository.save(post2);
        // When
        Post foundPost1 = repository.findOneByTitle(post1.getTitle()).get();
        Post foundPost2 = repository.findOneByTitle(post2.getTitle()).get();

        // Then
        org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> repository.findOneByTitle("title"));
        Assertions.assertThat(post1.getTitle()).isEqualTo(foundPost1.getTitle());
        Assertions.assertThat(post2.getTitle()).isEqualTo(foundPost2.getTitle());
    }
}