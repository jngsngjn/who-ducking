package hello.controller.board;

import hello.dto.board.BoardDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.board.Board;
import hello.entity.board.Bookmark;
import hello.entity.board.Comment;
import hello.entity.user.Level;
import hello.entity.user.ProfileImage;
import hello.entity.user.User;
import hello.repository.db.LevelRepository;
import hello.service.basic.UserService;
import hello.service.board.BoardService;
import hello.service.board.BookmarkService;
import hello.service.board.CommentService;
import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
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
    private final LevelRepository levelRepository;

    @GetMapping
    public String freeBoard(
                            Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @AuthenticationPrincipal CustomOAuth2User oAuth2User) {

        Page<Board> boardList;
        boardList = boardService.getBoardsSortedByLatest(page, 10);

        //boardList = boardService.getBoardsSortedByViewCount(page, 10);

        model.addAttribute("boardList", boardList);
        model.addAttribute("sort", "writeDate");
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", 10);

        boolean isAuthenticated = oAuth2User != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            User loginUser = userService.getLoginUserDetail(oAuth2User);
            Level level = loginUser.getLevel();
            Long afterLevel = level.getId();
            Long beforeLevel = afterLevel - 1L;

            if (beforeLevel > 0) {
                model.addAttribute("beforeLevelImage", levelRepository.findById(beforeLevel).get().getImageName());
                model.addAttribute("afterLevelImage", level.getImageName());
                model.addAttribute("afterLevel", afterLevel);
            }
        }
        return "/board/recentFreeBoard";
    }

    @GetMapping("/viewCount")
    public String freeBoardSortByViewCount(
                            Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @AuthenticationPrincipal CustomOAuth2User oAuth2User) {

        Page<Board> boardList;

        boardList = boardService.getBoardsSortedByViewCount(page, 10);

        model.addAttribute("boardList", boardList);
        model.addAttribute("sort", "viewCount");
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", 10);

        if (oAuth2User == null) {
            model.addAttribute("isAuthenticated", false);
        } else {
            model.addAttribute("isAuthenticated", true);
        }

        return "/board/viewCountFreeBoard";
    }

    //freeBoard -> 작성 폼을 띄워주는 역할
    @GetMapping("/new")
    public String showCreateBoard(Model model) {
        model.addAttribute("board", new BoardDTO());
        return "/board/writeFreeBoard";
    }

    //작성된 폼을 가지고 새로운 게시글 작성
    @PostMapping("/create")
    public String createBoard(@AuthenticationPrincipal CustomOAuth2User user,
                              @ModelAttribute("board") BoardDTO board,
                              @RequestParam("file") MultipartFile file,
                              HttpSession session) throws Exception {

        User loginUser = userService.getLoginUserDetail(user);
        boardService.createBoard(board, loginUser, file, session);

        String levelImageName = loginUser.getLevel().getImageName();
        ProfileImage profileImage = loginUser.getProfileImage();
        String profileImageName = null;
        if (profileImage != null) {
            profileImageName = profileImage.getStoreImageName();
        }
        session.setAttribute("levelImageName", levelImageName);
        session.setAttribute("profileImageName", profileImageName);

        return "redirect:/board";
    }

    //목록에서 해당 id에 맞는 게시글 상세보기
    @GetMapping("/{boardId}")
    public String showBoard(@PathVariable("boardId") Long boardId, Model model, @AuthenticationPrincipal CustomOAuth2User loginUser) {
        Board board = boardService.getBoardById(boardId).orElse(null);

        //null일경우 페이지 이동안함
        if (board == null) {
            return "redirect:/board";
        }

        if (loginUser == null) {
            model.addAttribute("isAuthenticated", false);
        } else {
            model.addAttribute("isAuthenticated", true);
            User user = userService.getLoginUserDetail(loginUser);
            model.addAttribute("loginUserId", user.getId());
            model.addAttribute("isBookmarked", bookmarkService.isBookmarked(user, board));
            model.addAttribute("nickname", user.getNickname());
        }

        List<Comment> comments = commentService.getCommentsByBoardId(boardId);

        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        return "board/show";
    }

    //조회수 증가
    @PostMapping("/{boardId}/incrementViewCount")
    public ResponseEntity<Void> incrementViewCount(@PathVariable("boardId") Long boardId) {
        boardService.updateViewCount(boardId);
        System.out.println("조회수 업데이트 성공");
        return ResponseEntity.ok().build();
    }

    //게시글 수정 폼
    @GetMapping("/{boardId}/edit")
    public String showEditBoard(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.getBoardById(boardId).orElse(null);
        if(board == null) {
            return "redirect:/board";
        }
        model.addAttribute("board", board);
        return "/board/editFreeBoard";
    }

    //게시글 수정 동작
    @PostMapping("/{boardId}/edit")
    public String editBoard(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal CustomOAuth2User user, @ModelAttribute("updatedBoard") BoardDTO updatedBoard, @RequestParam("file")MultipartFile file,
                            @RequestParam("useExistingImage") boolean useExistingImage) throws Exception {
        User loginUser = userService.getLoginUserDetail(user);

        if(useExistingImage){
            boardService.updateBoardWithoutChangingImage(boardId, updatedBoard, loginUser);
        }
        else{
            boardService.updateBoardWithNewImage(boardId, updatedBoard, loginUser, file);
        }
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
        boolean bookmarked = false;

        if (board != null) {
            if (!bookmarkService.isBookmarked(user, board)) {
                Bookmark bookmark = new Bookmark();
                bookmark.setUser(user);
                bookmark.setBoard(board);
                bookmarkService.addBookmark(bookmark);
                bookmarked = true;
            } else {
                bookmarkService.removeBookmark(user.getId(), board.getId());
            }
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("bookmarked", bookmarked);
        return response;
    }

    @GetMapping("/{boardId}/bookmark/status")
    @ResponseBody
    public Map<String, Boolean> getBookmarkStatus(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal CustomOAuth2User loginUser) {
        User user = userService.getLoginUserDetail(loginUser);
        Board board = boardService.getBoardById(boardId).orElse(null);
        boolean isBookmarked = false;

        if (board != null && bookmarkService.isBookmarked(user, board)) {
            isBookmarked = true;
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("bookmarked", isBookmarked);
        return response;
    }

    //신고
    @PostMapping("/{boardId}/report")
    @ResponseBody
    public void incrementReport(@PathVariable("boardId") Long boardId) {
        boardService.incrementReportCount(boardId);
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
            commentService.createComment(comment, board);
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