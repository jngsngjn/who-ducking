package hello.controller.board;

import hello.dto.board.BoardDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.board.Board;
import hello.entity.board.Bookmark;
import hello.entity.board.Comment;
import hello.entity.user.User;
import hello.service.board.BoardService;
import hello.service.board.BookmarkService;
import hello.service.board.CommentService;
import hello.service.basic.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@EnableSpringDataWebSupport
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final BookmarkService bookmarkService;
    private final CommentService commentService;

    @GetMapping
    public String freeBoard(@RequestParam(value = "sort", required = false, defaultValue = "writeDate") String sort,
                            Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page) {

        Page<Board> boardList;

        if ("viewCount".equals(sort)) {
            boardList = boardService.getBoardsSortedByViewCount(page, 10);
        } else {
            boardList = boardService.getBoardsSortedByLatest(page, 10);
        }


        model.addAttribute("boardList", boardList);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", 10);

        return "/board/freeBoard";
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
    public String showBoard(@PathVariable("boardId") Long boardId, Model model, @AuthenticationPrincipal CustomOAuth2User loginUser) {
        Board board = boardService.getBoardById(boardId).orElse(null);
        User user = userService.getLoginUserDetail(loginUser);

        //null일경우 페이지 이동안함
        if(board == null) {
            return "redirect:/board";
        }

        List<Comment>comments = commentService.getCommentsByBoardId(boardId);

        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        model.addAttribute("loginUserId", user.getId());
        model.addAttribute("isBookmarked", bookmarkService.isBookmarked(user, board));
        model.addAttribute("nickname", user.getNickname());
        return "board/show";
    }

    //조회수 증가
    @PostMapping("/{boardId}/incrementViewCount")
    @ResponseBody
    public void incrementViewCount(@PathVariable("boardId") Long boardId) {
        boardService.updateViewCount(boardId);
    }

    //게시글 수정 폼
    @GetMapping("/{boardId}/edit")
    public String showEditBoard(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.getBoardById(boardId).orElse(null);
        if(board == null) {
            return "redirect:/board";
        }
        model.addAttribute("board", board);
        return "board/edit";
    }

    //게시글 수정 동작
    @PostMapping("/{boardId}/edit")
    public String editBoard(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal CustomOAuth2User user, @ModelAttribute("updatedBoard") BoardDTO updatedBoard, @RequestParam("file")MultipartFile file) throws Exception {
        User loginUser = userService.getLoginUserDetail(user);
        boardService.updateBoard(boardId,updatedBoard,loginUser,file);
        return "redirect:/board/"+boardId;
    }

    //게시글 삭제 동작
    @PostMapping("/{boardId}/delete")
    public String deleteBoard(@PathVariable("boardId") Long boardId) throws NotFoundException {
        boardService.deleteBoard(boardId);
        return "redirect:/board";
    }

    //북마크 등록
    @PostMapping("/{boardId}/bookmark")
    @ResponseBody
    public Map<String, Boolean> toggleBookmark(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal CustomOAuth2User loginUser) {
        User user = userService.getLoginUserDetail(loginUser);
        Board board = boardService.getBoardById(boardId).orElse(null);
        boolean bookmarked;

        if (!bookmarkService.isBookmarked(user, board)) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setBoard(board);
            bookmarkService.addBookmark(bookmark);
            bookmarked = true;
        } else {
            assert board != null;
            bookmarkService.removeBookmark(user.getId(), board.getId());
            bookmarked = false;
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("bookmarked", bookmarked);
        return response;
    }

    //댓글 컨트롤러

    //댓글작성
    @PostMapping("/{boardId}/comment")
    public String createComment(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal CustomOAuth2User user, @RequestParam("contentWrite") String content) {
        User loginUser = userService.getLoginUserDetail(user);
        Board board = boardService.getBoardById(boardId).orElse(null);

        if (board != null) {
            Comment comment = new Comment();
            comment.setBoard(board);
            comment.setUser(loginUser);
            comment.setContent(content);
            commentService.createComment(comment);
        }

        return "redirect:/board/" + boardId;
    }

    // 댓글 삭제
    @PostMapping("/{boardId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId) throws NotFoundException {
        commentService.deleteComment(commentId);
        return "redirect:/board/" + boardId;
    }

    // 댓글 수정
    @PostMapping("/{boardId}/comment/{commentId}/edit")
    public String editComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, @RequestParam("contentUpdate") String content) throws NotFoundException {
        Comment updatedComment = new Comment();
        updatedComment.setContent(content);
        commentService.updateComment(commentId, updatedComment);
        return "redirect:/board/" + boardId;
    }
}