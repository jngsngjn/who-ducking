package hello.controller;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class MyPageController {

    private final UserService userService;

    @ModelAttribute("loginUser")
    public User getLoginUser(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getLoginUserDetail(user);
    }

    @GetMapping
    public String myPage(@ModelAttribute("loginUser") User loginUser, Model model) {
        model.addAttribute("loginUser", loginUser);
        return "myPage";
    }

    @GetMapping("/{userId}/edit")
    public String editPage(@ModelAttribute("loginUser") User loginUser, Model model) {
        model.addAttribute("loginUser", loginUser);
        return "editPage";
    }
}