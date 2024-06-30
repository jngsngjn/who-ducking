package hello.controller.mypage;

import hello.dto.board.MyBookmarkDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.basic.UserService;
import hello.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MyBookmarkController {

    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/myPage/bookmarks")
    public String myBookmarks(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              Model model) {

        User loginUser = userService.getLoginUserDetail(oAuth2User);
        Page<MyBookmarkDTO> myBookmarks = boardService.getMyBookmarks(loginUser, page, 5);
        model.addAttribute("myBookmarks", myBookmarks);
        return "mypage/myBookmark";
    }
}