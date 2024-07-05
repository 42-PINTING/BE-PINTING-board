package pinting.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pinting.board.controller.form.PostForm;
import pinting.board.domain.Post;
import pinting.board.service.BoardService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class BoardController {

    private final BoardService boardService;

    /**
     * 메인 페이지에 표출할 보드를 가져오는 메서드
     * @return 메인 페이지에 표출할 보드 10개 리스트 반훤
     */
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getMainPage() {
        return ResponseEntity.ok(boardService.getMainPagePost());
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody PostForm postForm) {
        Post post = new Post(postForm);
        boardService.createPost(post);
        return post;
    }

    @PutMapping("/posts/hidden/{id}")
    public String hiddenPost(@PathVariable Long id) {
        return "OK";
    }

}
