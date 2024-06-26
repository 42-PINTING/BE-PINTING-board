package pinting.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;

import java.util.List;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void createPostTest() {
        //given
        PostForm postForm = new PostForm();
        postForm.setImg("image_target1");
        postForm.setTitle("title_target1");
        postForm.setContent("content_target1");
        postForm.setAuthorId(110001L);
        postForm.setStatus("PUBLIC");
        Post post = new Post(postForm);

        //when
        Long postId = boardService.createPost(post);
        Post findPost = boardService.readOnePostById(postId).get();

        //then
        System.out.println("findPost = " + findPost);
        Assertions.assertThat(findPost.getId()).isEqualTo(post.getId());
        Assertions.assertThat(findPost.getContent()).isEqualTo(post.getContent());
        Assertions.assertThat(findPost.getImg()).isEqualTo(post.getImg());
    }

    @Test
    public void getMainPagePostTest() {
        //given
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


        //when
        Long post1Id = boardService.createPost(post1);
        Long post2Id = boardService.createPost(post2);
        List<Post> findPost = boardService.getMainPagePost();

        //then
        System.out.println("findPost = " + findPost);
        Assertions.assertThat(findPost.size()).isEqualTo(2);
        Assertions.assertThat(findPost.get(0).getId()).isEqualTo(post1Id);
        Assertions.assertThat(findPost.get(1).getId()).isEqualTo(post2Id);

    }
}