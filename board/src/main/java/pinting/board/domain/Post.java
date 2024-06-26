package pinting.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import pinting.board.controller.form.PostForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@ToString
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private Long authorId;
    private String title;
    private String img;
    private String content;
    private Long likeCount;
    private int hiddenTime;

    @OneToMany(mappedBy = "post")
    private List<Tag> tags = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private PostStatus status; // [PUBLIC, PRIVATE]

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime createdDate;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime updatedDate;
    // 삭제 날짜 필요한가?

    public Post(PostForm form) {
        this.authorId = form.getAuthorId();
        this.title = form.getTitle();
        this.img = form.getImg();
        this.content = form.getContent();
        if (form.getStatus().equals("public")) {
            this.status = PostStatus.PUBLIC;
        } else {
            this.status = PostStatus.PRIVATE;
        }
        this.likeCount = 0L;
    }

    protected Post() {
    }
}
