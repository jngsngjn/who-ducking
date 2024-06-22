package hello.controller.basic;

import hello.dto.user.CustomOAuth2User;
import hello.dto.user.HeaderDTO;
import hello.entity.user.User;
import hello.service.basic.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeaderController {

    private final UserService userService;

    @PostMapping("/update-header")
    public HeaderDTO handleRequest(@AuthenticationPrincipal CustomOAuth2User user, HttpSession session) {
        User loginUser = userService.getLoginUserDetail(user);

        Long level = loginUser.getLevel().getId();
        int point = loginUser.getPoint();
        int currentExp = loginUser.getCurrentExp();
        int maxExp = loginUser.getLevel().getMaxExp();

        session.setAttribute("level", level);
        session.setAttribute("point", point);
        session.setAttribute("currentExp", currentExp);
        session.setAttribute("maxExp", maxExp);

        return new HeaderDTO(point);
    }
}