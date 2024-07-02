package pinting.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import pinting.board.controller.form.PostForm;
import pinting.board.service.PostUpdateDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
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
    private LocalDateTime hiddenTime;

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

    public Post(Long authorId, String title, String img, String content, List<Tag> tags, PostStatus status) {
        this.authorId = authorId;
        this.title = title;
        this.img = img;
        this.content = content;
        this.tags = tags;
        this.status = status;
    }

    @Override
    public String toString() {
        return "[id: " + id +
                ", authorId: " + authorId +
                ", title: " + title +
                ", img: " + img +
                ", content: " + content +
                ", likeCount: " + likeCount + "]";
    }

    public void update(PostUpdateDto postUpdateDto) {
        this.title = postUpdateDto.getTitle();
        this.img = postUpdateDto.getImg();
        this.content = postUpdateDto.getContent();
        this.updatedDate = LocalDateTime.now();
        setTags(postUpdateDto.getTags());
    }

    private void setTags(List<String> tags) {
        this.tags.clear();
        for (String tag : tags) {
            this.tags.add(new Tag(tag));
        }
    }

    /**
     * like 증가
     */
    public Long increaseLikeCount() {
        this.likeCount++;
        return this.likeCount;
    }

    /**
     * like 감소
     */
    public Long decreaseLikeCount() {
        this.likeCount--;
        return this.likeCount;
    }

    /**
     * post 가리기
     */
    public void hiddenPost() {
        this.updatedDate = LocalDateTime.now();
        // TODO: hidden time 과 status 둘 중 하나만 사용해야 함.
        this.hiddenTime = LocalDateTime.now();
        this.status = PostStatus.PRIVATE;
    }

    /**
     * post 공개하기
     */
    public void publishPost() {
        this.updatedDate = LocalDateTime.now();
        this.hiddenTime = null;
        this.status = PostStatus.PUBLIC;
    }
}