package hello.controller.popup;

import hello.dto.user.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/popup")
public class PopupStoreController {

    @GetMapping()
    public String popupStore(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                             Model model) {
        model.addAttribute("isAuthenticated", oAuth2User != null);
        return "popup/popupStore";
    }
}