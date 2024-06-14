package hello.controller;

import hello.dto.board.BoardDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.board.Board;
import hello.entity.user.User;
import hello.service.BoardService;
import hello.service.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping
    public String freeBoard(@RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,Model model) {
        List<Board> boardList;
        if ("views".equals(sort)) {
            boardList = boardService.getBoardsSortedByViewCount();
        } else {
            boardList = boardService.getBoardsSortedByLatest();
        }

        model.addAttribute("boardList", boardList);

        model.addAttribute("sort", sort);

        return "board/freeBoard";
    }

    //freeBoard -> 작성 폼을 띄워주는 역할
    @GetMapping("/new")
    public String showCreateBoard(Model model) {
        model.addAttribute("board", new BoardDTO());
        return "board/create";
    }

    //작성된 폼을 가지고 새로운 게시글 작성
    @PostMapping("/create")
    public String createBoard(@AuthenticationPrincipal CustomOAuth2User user, @ModelAttribute("board") BoardDTO board, @RequestParam("file")MultipartFile file) throws Exception {
        User loginUser = userService.getLoginUserDetail(user);
        boardService.createBoard(board,loginUser,file);
        return "redirect:/board";
    }

    //목록에서 해당 id에 맞는 게시글 상세보기
    @GetMapping("/{boardId}")
    public String showBoard(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.getBoardById(boardId).orElse(null);

        //null일경우 페이지 이동안함
        if(board == null) {
            return "redirect:/board";
        }
        model.addAttribute("board", board);
        return "board/show";
    }

    //게시글 수정 폼
    @GetMapping("/{boardId}/edit")
    public String showEditBoard(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.getBoardById(boardId).orElse(null);
        if(board == null) {
            return "redirect:/board";
        }
        model.addAttribute("board", board);
        System.out.println(board.getImageName());
        return "board/edit";
    }

    @PostMapping("/{boardId}/edit")
    public String editBoard(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal CustomOAuth2User user, @ModelAttribute("updatedBoard") BoardDTO updatedBoard, @RequestParam("file")MultipartFile file) throws Exception {
        User loginUser = userService.getLoginUserDetail(user);
        boardService.updateBoard(boardId,updatedBoard,loginUser,file);
        return "redirect:/board";
    }

    @PostMapping("/{boardId}/delete")
    public String deleteBoard(@PathVariable("boardId") Long boardId) throws NotFoundException {
        boardService.deleteBoard(boardId);
        return "redirect:/board";
    }

}
