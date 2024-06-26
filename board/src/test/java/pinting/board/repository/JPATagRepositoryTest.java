package pinting.board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.domain.Tag;
import pinting.board.service.BoardService;

import java.util.List;

@SpringBootTest
class JPATagRepositoryTest {

    @Autowired
    JPATagRepository jpaTagRepository;

    @Autowired
    BoardService boardService;

    @Test
    public void 게시물_태그_삽입() {
        Post post = createPost("image", "title", "content", 4242L);
        boardService.createPost(post);
        Tag tag = createTag("tag", post);

        jpaTagRepository.save(tag);

        List<Tag> findTag = jpaTagRepository.findOneByPostId(post.getId());
        System.out.println("findTag.get(0).name = " + findTag.get(0).name);
        Assertions.assertThat(findTag).hasSize(1);
    }

    @Test
    public void 게시물_여러_태그_삽입() {
        Post post = createPost("image", "title", "content", 4242L);
        boardService.createPost(post);

        Tag summer = createTag("summer", post);
        jpaTagRepository.save(summer);

        Tag spring = createTag("spring", post);
        jpaTagRepository.save(spring);

        Tag fall = createTag("fall", post);
        jpaTagRepository.save(fall);


        List<Tag> findTag = jpaTagRepository.findOneByPostId(post.getId());
        System.out.println("findTag.get(0).name = " + findTag.get(0).name);
        System.out.println("findTag.get(1).name = " + findTag.get(1).name);
        System.out.println("findTag.get(2).name = " + findTag.get(2).name);

        Post findPost = boardService.readOnePostById(post.getId()).get();
        Assertions.assertThat(findTag).hasSize(3);
//        System.out.println(findPost.getTags());
    }

    private static Tag createTag(String name, Post post) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setPost(post);
        return tag;
    }

    private static Post createPost(String image, String title, String content, long authorId) {
        PostForm postForm = new PostForm();
        postForm.setImg(image);
        postForm.setTitle(title);
        postForm.setContent(content);
        postForm.setAuthorId(authorId);
        postForm.setStatus("PUBLIC");
        Post post = new Post(postForm);
        return post;
    }


}