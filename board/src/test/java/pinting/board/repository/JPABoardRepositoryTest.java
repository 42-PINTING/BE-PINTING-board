package pinting.board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.config.AppConfig;
import pinting.board.domain.Post;

import java.util.List;

@Transactional
@SpringBootTest(classes = AppConfig.class)
public class JPABoardRepositoryTest {

    @Test
    public void saveTest() {

        ApplicationContext ac = new AnnotationConfigReactiveWebApplicationContext(AppConfig.class);
        JPABoardRepository repository = ac.getBean(JPABoardRepository.class);

        Post post = new Post();
        post.setImg("image");
        post.setTitle("title");
        post.setContent("content");
        post.setAuthor_id(10010L);

        repository.save(post);
        Post findPost = repository.findOneById(post.getId()).orElse(new Post());
        System.out.println("post = " + findPost.toString());
        Assertions.assertThat(post.getTitle()).isEqualTo(findPost.getTitle());
    }

    @Test
    public void findByAllTest() {

        ApplicationContext ac = new AnnotationConfigReactiveWebApplicationContext(AppConfig.class);
        JPABoardRepository repository = ac.getBean(JPABoardRepository.class);

        Post post1 = new Post();
        post1.setImg("image_target1");
        post1.setTitle("title_target1");
        post1.setContent("content_target1");
        post1.setAuthor_id(110001L);

        Post post2 = new Post();
        post2.setImg("image_target2");
        post2.setTitle("title_target2");
        post2.setContent("content_target2");
        post2.setAuthor_id(110002L);

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
        Post post1 = new Post();
        post1.setImg("image_target1");
        post1.setTitle("title_target1");
        post1.setContent("content_target1");
        post1.setAuthor_id(authorId);

        Post post2 = new Post();
        post2.setImg("image_target2");
        post2.setTitle("title_target2");
        post2.setContent("content_target2");
        post2.setAuthor_id(authorId);

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
        Post post1 = new Post();
        post1.setImg("image_target1");
        post1.setTitle("title_target1");
        post1.setContent("content_target1");
        post1.setAuthor_id(authorId);

        Post post2 = new Post();
        post2.setImg("image_target2");
        post2.setTitle("title_target2");
        post2.setContent("content_target2");
        post2.setAuthor_id(authorId);

        repository.save(post1);
        repository.save(post2);
        // When
        Post foundPost1 = repository.findOneByTitle(post1.getTitle()).orElse(new Post());
        Post foundPost2 = repository.findOneByTitle(post2.getTitle()).orElse(new Post());

        // Then
        org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> repository.findOneByTitle("title"));
        Assertions.assertThat(post1.getTitle()).isEqualTo(foundPost1.getTitle());
        Assertions.assertThat(post2.getTitle()).isEqualTo(foundPost2.getTitle());
    }
}