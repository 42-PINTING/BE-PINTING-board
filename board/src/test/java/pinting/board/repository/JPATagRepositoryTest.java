package pinting.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.domain.QTag;
import pinting.board.domain.Tag;
import pinting.board.service.BoardService;

import javax.swing.text.html.parser.Entity;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pinting.board.domain.QTag.tag;

@SpringBootTest
@Transactional
class JPATagRepositoryTest {

    @Autowired
    JPATagRepository jpaTagRepository;

    @Autowired
    EntityManager em;

    @Autowired
    BoardService boardService;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        PostForm form1 = new PostForm();
        Post postA = new Post(form1);
        PostForm form2 = new PostForm();
        Post postB = new Post(form2);
        PostForm form3 = new PostForm();
        Post postC = new Post(form3);
        PostForm form4 = new PostForm();
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

    @Test
    public void 태그_생성() {
        jpaTagRepository.save(new Tag("summer"));
        jpaTagRepository.save(new Tag("winter"));

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
    public void 태그_검색() {
        List<Tag> results = jpaTagRepository.findAllByName("diary");

        for (Tag tag : results) {
            System.out.println("tag = " + tag);
        }

        assertThat(results).extracting("name").contains("diary");
    }

    @Test
    public void 게시물_아이디_검색() {
        List<Tag> results = jpaTagRepository.findAllByPostId(4L);

        for (Tag tag : results) {
            System.out.println("tag = " + tag);
        }

        assertThat(results.size()).isEqualTo(2);
    }
}