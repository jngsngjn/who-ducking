package hello.controller.basic;

import hello.dto.user.CustomOAuth2User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SessionController {

    @ResponseBody
    @PostMapping("/check-levelUp-session")
    public boolean checkLevelUpSession(HttpSession session, @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return false;
        }

        Boolean isLevelUp = (Boolean) session.getAttribute("levelUp");

        if (isLevelUp != null && isLevelUp) {
            // 세션이 유효할 경우
            return true;
        } else {
            // 세션이 유효하지 않을 경우
            return false;
        }
    }

    @ResponseBody
    @PostMapping("/delete-levelUp-session")
    public boolean deleteLevelUpSession(HttpSession session) {
        try {
            session.removeAttribute("levelUp");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}