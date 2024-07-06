package pinting.board.dto;

import lombok.Getter;
import lombok.Setter;
import pinting.board.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostReturnDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String img;
    // TODO: 자료형의 메서드의 반환 값이 int, int를 넘어가면 어떻게 처리하는가?
    private final int likeCount;
    private final Long viewCount;
    private final List<String> tags;
    private final LocalDateTime hiddenTime;
    private final LocalDateTime createdDate;
    private final LocalDateTime updatedDate;

    public PostReturnDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.img = post.getImg();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.tags = post.getTagNames();
        this.hiddenTime = post.getHiddenTime();
        this.createdDate = post.getCreatedDate();
        this.updatedDate = post.getUpdatedDate();
    }

    @Override
    public String toString() {
        return "[id=" + id + ", title=" + title + ", content=" + content + ", img=" + img
                + ", likeCount=" + likeCount + ", viewCount=" + viewCount + ", tags=" + tags
                + ", hiddenTime=" + hiddenTime + ", createdDate=" + createdDate + ", updatedDate="
                + updatedDate + "]";
    }
}
