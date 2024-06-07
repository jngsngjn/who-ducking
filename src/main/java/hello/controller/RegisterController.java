package hello.controller;

import hello.dto.oauth2.CustomOAuth2User;
import hello.dto.user.RegisterDTO;
import hello.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/register")
    public String registerForm(@AuthenticationPrincipal CustomOAuth2User user,
                               Model model) {
        String email = user.getEmail();
        model.addAttribute("email", email);
        return "register";
    }

    @PostMapping("/register")
    public String register(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                           @ModelAttribute RegisterDTO registerDTO) {
        registerService.registerProcess(oAuth2User, registerDTO);
        return "redirect:/";
    }
}