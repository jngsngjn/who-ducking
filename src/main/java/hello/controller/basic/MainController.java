package hello.controller.basic;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.ProfileImage;
import hello.entity.user.User;
import hello.service.basic.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal CustomOAuth2User user, HttpSession session) {
        if (user != null) {

            User loginUser = userService.getLoginUserDetail(user);

            if (loginUser != null) {
                String nickname = loginUser.getNickname();
                String levelImageName = loginUser.getLevel().getImageName();
                ProfileImage profileImage = loginUser.getProfileImage();
                String profileImageName = null;
                if (profileImage != null) {
                    profileImageName = profileImage.getStoreImageName();
                    System.out.println("profileImageName = " + profileImageName);
                }

                session.setAttribute("nickname", nickname);
                session.setAttribute("levelImageName", levelImageName);
                session.setAttribute("profileImageName", profileImageName);
                session.setAttribute("level", loginUser.getLevel().getId());
                session.setAttribute("point", loginUser.getPoint());
                session.setAttribute("currentExp", loginUser.getCurrentExp());
                session.setAttribute("maxExp", loginUser.getLevel().getMaxExp());

                String role = loginUser.getRole();
                if (role.equals("ROLE_ADMIN")) {
                    session.setAttribute("isAdmin", true);
                }
            }
        }
        return "index";
    }
}