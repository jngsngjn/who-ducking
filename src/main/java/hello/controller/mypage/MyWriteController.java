package hello.controller.mypage;

import hello.dto.board.MyBoardDTO;
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
public class MyWriteController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/myPage/boards")
    public String myBoard(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          Model model) {

        User loginUser = userService.getLoginUserDetail(oAuth2User);
        Page<MyBoardDTO> myBoardsSortedDate = boardService.getMyBoardsOrderByWriteDate(loginUser, page, 5);
        model.addAttribute("myBoardsDate", myBoardsSortedDate);
        return "mypage/myWrite";
    }

    @GetMapping("/myPage/boards/viewCount")
    public String myBoardViewCount(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          Model model) {

        User loginUser = userService.getLoginUserDetail(oAuth2User);
        Page<MyBoardDTO> myBoardsSortedViewCount = boardService.getMyBoardsOrderByViewCount(loginUser, page, 5);
        model.addAttribute("myBoardsView", myBoardsSortedViewCount);
        return "mypage/myWriteOrderByViewCount";
    }
}