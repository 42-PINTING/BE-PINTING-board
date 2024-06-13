package pinting.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private Long author_id;
    private String title;
    private String img;
    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus status; // [PUBLIC, PRIVATE]

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    // 삭제 날짜 필요한가?
}
