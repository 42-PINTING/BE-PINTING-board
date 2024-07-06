package pinting.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostUpdateDto {
    private String title;
    private String img;
    private String content;
    private List<String> tags;

    public PostUpdateDto(String title, String img, String content, List<String> tags) {
        this.title = title;
        this.img = img;
        this.content = content;
        this.tags = tags;
    }
}
