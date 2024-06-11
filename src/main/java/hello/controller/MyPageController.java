package hello.controller;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.UserService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping
    public String myPage(@AuthenticationPrincipal CustomOAuth2User user, Model model, HttpSession session) {
        User loginUser = userService.getLoginUserDetail(user);
        model.addAttribute("loginUser", loginUser);
        session.setAttribute("loginUser", loginUser);
        return "myPage";
    }

    @GetMapping("/{userId}/edit")
    public String editPage(Model model) {
        return "editPage";
    }
}