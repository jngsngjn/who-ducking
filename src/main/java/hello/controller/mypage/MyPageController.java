package hello.controller.mypage;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;

    // 내 정보 페이지 보여주기
    @GetMapping
    public String myPage(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        User loginUser = userService.getLoginUserDetail(user);
        model.addAttribute("loginUser", loginUser);
        return "myPage";
    }
}