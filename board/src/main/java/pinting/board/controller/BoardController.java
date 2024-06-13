package pinting.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pinting.board.controller.dto.PostDto;
import pinting.board.domain.Post;
import pinting.board.service.BoardService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 메인 페이지에 표출할 보드를 가져오는 메서드
     * @return 메인 페이지에 표출할 보드 10개 리스트 반훤
     */
    @GetMapping("/posts")
    @ResponseBody
    public ResponseEntity<List<Post>> getMainPage() {
        return ResponseEntity.ok(boardService.getMainPagePost());
    }

    @PostMapping("/posts")
    @ResponseBody
    public String createPost(PostDto postDto) {
        Post post = new Post();
        post.setAuthor_id(postDto.getAuthor_id());
        post.setImg(postDto.getImg());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        boardService.createPost(post);
        return "OK";
    }
}
