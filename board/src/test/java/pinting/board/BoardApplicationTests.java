package pinting.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.domain.QPost;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardApplicationTests {

	@Autowired
	private EntityManager em ;

	@Test
	void contextLoads() {
		PostForm form = new PostForm();
		form.setStatus("PUBLIC");
		form.setTitle("title title title");
		form.setContent("Connnnnnntent");

		Post post = new Post(form);
		em.persist(post);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QPost qPost = new QPost("p");

		Post findpost = query
				.selectFrom(qPost)
				.fetchOne();
		System.out.println("findpost = " + findpost.getTitle());
		assertThat(findpost.getId()).isEqualTo(post.getId());
	}

}
