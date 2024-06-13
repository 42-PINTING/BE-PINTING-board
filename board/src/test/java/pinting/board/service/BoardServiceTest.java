package pinting.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
        Post post = new Post();
        post.setContent("content1");
        post.setTitle("title2");
        post.setImg("image3");
        post.setAuthor_id(4242L);

        //when
        Long postId = boardService.createPost(post);
        Post findPost = boardService.readOnePostById(postId).orElse(new Post());

        //then
        System.out.println("findPost = " + findPost);
        Assertions.assertThat(findPost.getId()).isEqualTo(post.getId());
        Assertions.assertThat(findPost.getContent()).isEqualTo(post.getContent());
        Assertions.assertThat(findPost.getImg()).isEqualTo(post.getImg());
    }

    @Test
    public void getMainPagePostTest() {
        //given
        Post post1 = new Post();
        post1.setContent("content1");
        post1.setTitle("title2");
        post1.setImg("image3");
        post1.setAuthor_id(4242L);

        Post post2 = new Post();
        post2.setContent("content1");
        post2.setTitle("title2");
        post2.setImg("image3");
        post2.setAuthor_id(4242L);

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