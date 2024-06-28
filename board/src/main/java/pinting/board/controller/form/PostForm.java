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
//    private List<String> tags;

    /**
     * Constructor for Test
     */
    static long i = 0;
    public PostForm() {
        this.authorId = i++;
        this.title = "title" + authorId;
        this.img = "img" + authorId;
        this.content = "content" + authorId;
        this.status = "PUBLIC";
    }
}
