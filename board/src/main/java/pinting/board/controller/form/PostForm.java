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
    private List<String> tags;

    public PostForm(Long authorId, String title, String img, String content, List<String> tags) {
        this.authorId = authorId;
        this.title = title;
        this.img = img;
        this.content = content;
        this.tags = tags;
    }
}
