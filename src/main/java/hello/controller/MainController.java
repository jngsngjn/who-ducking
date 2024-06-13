package hello.controller;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.ProfileImage;
import hello.entity.user.User;
import hello.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        if (user != null) {

            User loginUser = userService.getLoginUserDetail(user);

            if (loginUser != null) {
                String nickname = loginUser.getNickname();
                String levelImageName = loginUser.getLevel().getImageName();
                ProfileImage profileImage = loginUser.getProfileImage();
                String profileImageName = null;
                if (profileImage != null) {
                    profileImageName = profileImage.getStoreImageName();
                }

                model.addAttribute("nickname", nickname);
                model.addAttribute("levelImageName", levelImageName);
                model.addAttribute("profileImageName", profileImageName);
            }
        }
        return "main";
    }
}