package hello.controller.mypage;

import hello.dto.board.BookmarkDTO;
import hello.dto.board.MyBookmarkDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.board.Board;
import hello.entity.board.Bookmark;
import hello.entity.user.User;
import hello.service.basic.UserService;
import hello.service.board.BoardService;
import hello.service.board.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MyBookmarkController {

    private final UserService userService;
    private final BoardService boardService;
    private final BookmarkService bookmarkService;

    @GetMapping("/myPage/bookmarks")
    public String myBookmarks(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              Model model) {

        User loginUser = userService.getLoginUserDetail(oAuth2User);
        Page<MyBookmarkDTO> myBookmarks = boardService.getMyBookmarks(loginUser, page, 5);
        model.addAttribute("myBookmarks", myBookmarks);
        return "mypage/myBookmark";
    }

    @ResponseBody
    @PostMapping("/myPage/bookmark/delete")
    public void deleteBookmark(@ModelAttribute BookmarkDTO bookmarkDTO,
                               @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        Long userId = userService.getLoginUserDetail(oAuth2User).getId();
        Long boardId = bookmarkDTO.getId();
        bookmarkService.removeBookmark(userId, boardId);
    }

    @ResponseBody
    @PostMapping("/myPage/bookmark/add")
    public void addBookmark(@ModelAttribute BookmarkDTO bookmarkDTO,
                            @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        User user = userService.getLoginUserDetail(oAuth2User);
        Long boardId = bookmarkDTO.getId();

        Optional<Board> board = boardService.getBoardById(boardId);
        if (board.isPresent()) {

            Bookmark bookmark = new Bookmark();
            bookmark.setBoard(board.get());
            bookmark.setUser(user);
            bookmarkService.addBookmark(bookmark);
        }
    }
}