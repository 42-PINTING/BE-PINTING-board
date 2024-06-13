package pinting.board.controller.dto;

import lombok.Getter;

@Getter
public class PostDto {
    private Long author_id;
    private String title;
    private String img;
    private String content;
}
