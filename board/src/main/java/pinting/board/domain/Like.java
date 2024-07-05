package pinting.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes", uniqueConstraints =
    @UniqueConstraint(name = "UniqueMemberAndPost", columnNames = {"post_id", "memberId"})
)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Override
    public String toString() {
        return "Like [id=" + id +
                ", memberId=" + memberId + "]";
    }

    public Like(Post post, Long memberId) {
        this.post = post;
        post.getLikes().add(this);
        this.memberId = memberId;
    }
}
