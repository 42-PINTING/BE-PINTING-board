package pinting.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pinting.board.domain.Like;
import pinting.board.domain.Post;
import pinting.board.domain.QLike;
import pinting.board.domain.QPost;

import java.util.List;
import java.util.Optional;

import static pinting.board.domain.QLike.*;
import static pinting.board.domain.QPost.post;

@Repository
@RequiredArgsConstructor
@Transactional
public class JPALikeRepository implements LikeRepository{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(Like like) {
        em.persist(like);
    }

    @Override
    public void deleteById(Long id) {
        Like existing = em.find(Like.class, id);
        if (existing != null) {
            this.em.remove(this.em.contains(existing) ? existing : this.em.merge(existing));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Like> findOneById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(like)
                .where(like.id.eq(id))
                .fetchOne());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Like> findAllByPostId(Long postId) {
        return queryFactory
                .selectFrom(like)
                .join(like.post, post)
                .where(like.post.id.eq(postId))
                .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Like> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(like)
                .where(like.memberId.eq(memberId))
                .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Like> findAll() {
        return queryFactory
                .selectFrom(like)
                .fetch();
    }

    @Override
    public Optional<Like> findOneByMemberIdAndPostId(Long memberId, Long postId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(like)
                .join(like.post, post)
                .where(like.post.id.eq(postId), like.memberId.eq(memberId))
                .fetchOne());
    }
}
