package pinting.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.domain.Tag;
import pinting.board.service.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pinting.board.domain.QTag.tag;

@SpringBootTest
@Transactional
class JPATagRepositoryTest {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    EntityManager em;

    @Autowired
    BoardService boardService;

    @Autowired
    JPAQueryFactory queryFactory;

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

        Tag diary1 = new Tag("diary", postA);
        Tag diary2 = new Tag("diary", postD);
        Tag feeling1 = new Tag("feeling", postC);
        Tag feeling2 = new Tag("feeling", postD);
        em.persist(diary1);
        em.persist(diary2);
        em.persist(feeling1);
        em.persist(feeling2);

    }

    public PostForm createForm(Long id) {
        List<String> tags = new ArrayList<>();
        return new PostForm(id, "title " + id, "img " + id, "content " + id, "public", tags);
    }

    @Test
    public void 태그_생성() {
        tagRepository.save(new Tag("summer"));
        tagRepository.save(new Tag("winter"));

        Tag summer = queryFactory
                .selectFrom(tag)
                .where(tag.name.eq("summer"))
                .fetchOne();

        Tag winter = queryFactory
                .selectFrom(tag)
                .where(tag.name.eq("winter"))
                .fetchOne();


        assertThat(summer.getName()).isEqualTo("summer");
        assertThat(winter.getName()).isEqualTo("winter");
    }

    @Test
    public void 태그_삭제() {
        tagRepository.deleteById(1L);

        Optional<Tag> findTag = tagRepository.findOneById(1L);

        assertThat(findTag.isEmpty()).isTrue();
    }

    @Test
    public void 태그_이름_검색() {
        List<Tag> results = tagRepository.findAllByName("diary");

        for (Tag tag : results) {
            System.out.println("tag = " + tag);
        }

        assertThat(results).extracting("name").contains("diary");
    }

    @Test
    public void 게시물_아이디_검색() {
        PostForm form5 = createForm(5L);
        Post postE = new Post(form5);
        em.persist(postE);

        Long postId = postE.getId();

        tagRepository.save(new Tag("diary", postE));
        tagRepository.save(new Tag("babe", postE));

        List<Tag> results = tagRepository.findAllByPostId(postId);

        for (Tag tag : results) {
            System.out.println("tag = " + tag);
        }

        assertThat(results.size()).isEqualTo(2);
    }

}