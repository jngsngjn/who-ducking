package hello.controller;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        if (user != null) {
            String username = user.getUsername();
            User findUser = userRepository.findByUsername(username);

            if (findUser != null) {
                String nickname = findUser.getNickname();
                String imageName = findUser.getLevel().getImageName();
                model.addAttribute("nickname", nickname);
                model.addAttribute("imageName", imageName);
            }
        }
        return "main";
    }
}