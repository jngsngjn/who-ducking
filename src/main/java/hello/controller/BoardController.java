package hello.controller;

import hello.entity.board.Board;
import hello.service.BoardService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }



    @GetMapping("/boardList")
    public String boardList(Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "board/boardList";
    }

    @GetMapping("/write")
    public String writeBoardPage(Model model) {
        model.addAttribute("board", new Board());
        return "board/write";
    }

    @PostMapping("/write")
    public String writeBoard(@ModelAttribute("board") Board board) {
        boardService.createBoard(board);
        return "redirect:/board/boardList";
    }

    @GetMapping("/{boardId}")
    public String viewBoard(@PathVariable Long boardId, Model model) {
        Board board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
        return "board/view";
    }

    @GetMapping("/{boardId}/edit")
    public String editBoardPage(@PathVariable Long boardId, Model model) {
        Board board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
        return "board/edit";
    }

    @PostMapping("/{boardId}/edit")
    public String editBoard(@PathVariable Long boardId, @ModelAttribute("board") Board updatedBoard) throws NotFoundException {
        boardService.updateBoard(boardId, updatedBoard);
        return "redirect:/board/" + boardId;
    }

    @PostMapping("/{boardId}/delete")
    public String deleteBoard(@PathVariable Long boardId) throws NotFoundException {
        boardService.deleteBoard(boardId);
        return "redirect:/board/boardList";
    }


}
