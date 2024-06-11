package hello.controller;

import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;

//    @GetMapping("/")
//    public String mainPage(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
//        if (user != null) {
//            String username = user.getUsername();
//            User findUser = userRepository.findByUsername(username);
//
//            if (findUser != null) {
//                String nickname = findUser.getNickname();
//                String levelImagePath = findUser.getLevel().getImagePath();
//                System.out.println("levelImagePath = " + levelImagePath);
//
//                model.addAttribute("nickname", nickname);
//                model.addAttribute("levelImagePath", levelImagePath + ".jpeg");
//            }
//        }
//        return "main";
//    }

    @GetMapping("/")
    public String mainTest(Model model) {
        String levelImagePath = "level1.jpeg";
        System.out.println("Setting levelImagePath = " + levelImagePath);
        model.addAttribute("levelImagePath", levelImagePath);
        return "main";
    }

}