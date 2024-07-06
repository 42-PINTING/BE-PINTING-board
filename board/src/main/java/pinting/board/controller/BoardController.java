package pinting.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.dto.PostReturnDto;
import pinting.board.service.BoardService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class BoardController {

    private final BoardService boardService;

    /**
     * 메인 페이지에 표출할 보드를 가져오는 메서드
     * TODO: 엔티티를 직접 반환하는 것은 올바르지 않음.
     * @return 메인 페이지에 표출할 보드 9개 리스트 반훤
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostReturnDto>> getMainPage() {
        return ResponseEntity.ok(boardService.getMainPagePost());
    }

    @PostMapping("/posts")
    public PostReturnDto createPost(@RequestBody PostForm postForm) {
        Post post = new Post(postForm);
        boardService.createPost(post);
        return new PostReturnDto(post);
    }

    @PutMapping("/posts/hidden/{id}")
    public String hiddenPost(@PathVariable Long id) {
        return "OK";
    }

}
