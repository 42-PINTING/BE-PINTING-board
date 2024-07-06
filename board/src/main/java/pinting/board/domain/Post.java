package pinting.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import pinting.board.controller.form.PostForm;
import pinting.board.dto.PostUpdateDto;

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
    private Long viewCount;
    private LocalDateTime hiddenTime;

    @OneToMany(mappedBy = "post")
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Like> likes = new ArrayList<>();

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
        this.viewCount = 0L;
        this.hiddenTime = LocalDateTime.now();
        for (String tagName : form.getTags()) {
            new Tag(tagName, this);
        }
    }

    public Post(Long authorId, String title, String img, String content, List<Tag> tags) {
        this.authorId = authorId;
        this.title = title;
        this.img = img;
        this.content = content;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "[id: " + id +
                ", authorId: " + authorId +
                ", title: " + title +
                ", img: " + img +
                ", content: " + content + "]";
    }

    public void update(PostUpdateDto postUpdateDto) {
        this.title = postUpdateDto.getTitle();
        this.img = postUpdateDto.getImg();
        this.content = postUpdateDto.getContent();
        this.updatedDate = LocalDateTime.now();
    }

    /**
     * like 개수 반환
     */
    public int getLikeCount() {
        return this.likes.size();
    }

    /**
     * 태그 이름만 반환
     */
    public List<String> getTagNames() {
        return this.tags.stream().map(Tag::getName).toList();
    }

    /**
     * post 가리기
     */
    public void hiddenPost() {
        this.updatedDate = LocalDateTime.now();
        this.hiddenTime = LocalDateTime.now();
    }

    /**
     * post 공개하기
     */
    public void publishPost() {
        this.updatedDate = LocalDateTime.now();
        this.hiddenTime = null;
    }
}