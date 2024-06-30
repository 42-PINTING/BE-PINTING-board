package pinting.board.service;

import lombok.Getter;

import java.util.List;

@Getter
public class PostUpdateDto {
    private String title;
    private String img;
    private String content;
    private List<String> tags;
}
