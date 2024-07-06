package pinting.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.domain.QPost;
import pinting.board.domain.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static pinting.board.domain.QPost.post;
import static pinting.board.domain.QTag.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    /*@Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

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

    private PostForm createForm(Long id) {
        return new PostForm(id, "title " + id, "img " + id, "content " + id, "public", null);
    }

    @Test
    @Rollback(value = false)
    public void startJPQL() {
//        Post findPost = em.createQuery("select p from Post p where p.title = :title", Post.class)
//                .setParameter("title", "title1")
//                .getSingleResult();

//        assertThat(findPost.getTitle()).isEqualTo("title1");

        List<Post> results = em.createQuery("select p from Post p", Post.class)
                .getResultList();
        for (Post post : results) {
            System.out.println(post.getTags());
        }
    }

    @Test
    public void startQuerydsl() {
        // querydsl로 작성한 코드는 JPQL이 된다.
        Post findPost = queryFactory
                .select(post)
                .from(post)
                .where(post.title.eq("title1")) //JDBC prepare statement로 자동으로 바인딩을 해준다.
                // 오타가 생겼을 때 컴파일타임에 요유를 잡아준다. Qtype을 생성하는 이유다.
                .fetchOne();
        List<Post> results = queryFactory
                .selectFrom(post)
                .fetch();
        System.out.println("results = " + results);

        System.out.println("findPost = " + findPost);
        assertThat(findPost.getTitle()).isEqualTo("title1");
    }

    @Test
    public void search() {
        Post findPost = queryFactory
                .selectFrom(post)
                .where(post.title.eq("title1").and(post.content.contains("content")))
                .fetchOne();

        System.out.println("findPost = " + findPost);
        assertThat(findPost.getTitle()).isEqualTo("title1");
    }

    *//**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순
     * 2. 회원 나이 오름차순
     *  이름이 없으면 마지막에 출력(nulls last)
     *//*
    @Test
    public void sort() {
        PostForm form1 = createForm(4L);
        form1.setContent("hi");
        form1.setTitle(null);
        em.persist(new Post(form1));
        PostForm form2 = createForm(5L);
        form2.setContent("hi");
        em.persist(new Post(form2));
        PostForm form3 = createForm(6L);
        form3.setContent("hi");
        em.persist(new Post(form3));

        List<Post> results = queryFactory
                .selectFrom(post)
                .where(post.content.eq("hi"))
                .orderBy(post.content.desc(), post.title.asc().nullsLast())
                .fetch();

        Post post1 = results.get(0);
        Post post2 = results.get(1);
        Post post3 = results.get(2);

        for (Post post : results) {
            System.out.println("post = " + post);
        }
        assertThat(post3.getTitle()).isNull();
    }

    @Test
    public void paging1() {
        List<Post> results = queryFactory
                .selectFrom(post)
                .orderBy(post.id.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    public void paging2() {
        QueryResults<Post> results = queryFactory
                .selectFrom(post)
                .orderBy(post.id.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(results.getTotal()).isEqualTo(4);
        assertThat(results.getLimit()).isEqualTo(2);
        assertThat(results.getOffset()).isEqualTo(1);
        assertThat(results.getResults().size()).isEqualTo(2);
    }


    @Test
    public void aggregation() {
        List<Tuple> results = queryFactory
                .select(
                        post.count(),
                        post.id.sum(),
                        post.id.avg(),
                        post.id.max(),
                        post.id.min()
                )
                .from(post)
                .fetch();

        Tuple tuple = results.get(0); // 실무에서는 DTO로 조회하는 방법을 사용한다.
        for( int i = 0; i < results.size(); i++ ) {
            System.out.println("results.get(i) = " + results.get(i));
        }
        assertThat(tuple.get(post.count())).isEqualTo(4);
    }


    // 태그의 이름과 id 평균 구하
    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(tag.name, post.id.avg())
                .from(post)
                .join(post.tags, tag)
                .groupBy(tag.name)
                .fetch();

        Tuple tag1 = result.get(0);
        Tuple tag2 = result.get(1);

        System.out.println("tag1 = " + tag1);
        System.out.println("tag2 = " + tag2);

    }

    @Test
    public void join() {
        List<Post> diaries = queryFactory
                .selectFrom(post)
//                .join(post.tags, tag)
//                .where(tag.name.eq("diary"))
                .fetch();
        for (Post post : diaries) {
            System.out.println("post의 tags = " + post.getTitle() + " " + post.getTags());
        }
//        assertThat(diaries.size()).isEqualTo(2);
    }


    *//**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: select p, t from Post p left join p.tag t on tag.name = 'diary'
     *//*
    @Test
    public void join_on_filtering() {
        List<Tuple> results = queryFactory
                .select(post.id, post.title, tag)
                .from(post)
//                .join(post.tags, tag)
                .leftJoin(post.tags, tag)
                .on(tag.name.eq("diary")) // outter join 인 경우에만 on절이 의미가 있다.
                // inner join한 뒤 where 절로 필터링 하는 것과 결과가 동일하다.
                .fetch();

        for (Tuple tuple : results) {
            System.out.println("tuple = " + tuple);
        }

    }

    @Test
    public void subQuery() {
        QPost postSub = new QPost("postSub");
        List<Post> results = queryFactory
                .selectFrom(post)
                .where(post.id.eq(
                        JPAExpressions
                                .select(postSub.id.max())
                                .from(postSub)
                ))
                .fetch();

        for (Post post : results) {
            System.out.println("post = " + post);
        }
        assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void subQueryGoe() {
        QPost postSub = new QPost("postSub");
        List<Post> results = queryFactory
                .selectFrom(post)
                .where(post.id.goe(
                        JPAExpressions
                                .select(postSub.id.avg())
                                .from(postSub)
                ))
                .fetch();

        for (Post post : results) {
            System.out.println("post = " + post);
        }
        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    public void subQueryIn() {
        QPost postSub = new QPost("postSub");
        List<Post> results = queryFactory
                .selectFrom(post)
                .where(post.id.in(
                        JPAExpressions
                                .select(postSub.id)
                                .from(postSub)
                                .where(postSub.id.gt(3))
                ))
                .fetch();

        for (Post post : results) {
            System.out.println("post = " + post);
        }
        assertThat(results.size()).isEqualTo(1);
        assertThat(results).extracting("id").containsExactly(4L);
    }

    @Test
    public void selectSubQuery() {
        QPost postSub = new QPost("postSub");
        List<Tuple> results = queryFactory
                .select(post.title,
                        JPAExpressions
                                .select(postSub.id.avg())
                                .from(postSub))
                .from(post)
                .fetch();

        for (Tuple tuple : results) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void dynamicQuery_Boolean_builder() {
        String contentParam = "content";
        Long idParam = null;

        List<Post> result = searchPost1(contentParam, idParam);
        for (Post post : result) {
            System.out.println("post = " + post);
        }
        assertThat(result.size()).isEqualTo(4);
    }

    private List<Post> searchPost1(String contentCond, Long idCond) {
        BooleanBuilder builder = new BooleanBuilder();

        if (contentCond != null) {
            builder.and(post.content.startsWith(contentCond));
        }

        if (idCond != null) {
            builder.and(post.id.eq(idCond));
        }

        return queryFactory
                .selectFrom(post)
                .where(builder)
                .fetch();
    }

    @Test
    public void dynamicQuery_WhereParam() {
        String contentParam = "content";
        Long idParam = null;

        List<Post> result = searchPost2(contentParam, idParam);
        for (Post post : result) {
            System.out.println("post = " + post);
        }
        assertThat(result.size()).isEqualTo(4);
    }

    public List<Post> searchPost2(String contentCond, Long idCond) {
        return queryFactory
                .selectFrom(post)
                .where(contentLike(contentCond), idEq(idCond))
                .fetch();
    }

    private Predicate idEq(Long idCond) {
        if (idCond == null) {
            return null;
        }
        return post.id.eq(idCond);
    }

    private Predicate contentLike(String contentCond) {
        if (contentCond != null) {
            return post.content.startsWith(contentCond);
        }
        return null;
    }*/
}
