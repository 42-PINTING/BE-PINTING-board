package pinting.board.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class PostForm {
    private Long authorId;
    private String title;
    private String img;
    private String content;
    private String status;
    private List<String> tags;

    public PostForm(Long authorId, String title, String img, String content, String status, List<String> tags) {
        this.authorId = authorId;
        this.title = title;
        this.img = img;
        this.content = content;
        this.status = status;
        this.tags = tags;
    }
}
