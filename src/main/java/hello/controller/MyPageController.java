package hello.controller;

import hello.dto.user.CustomOAuth2User;
import hello.dto.user.EditDTO;
import hello.entity.user.User;
import hello.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;

    // 내 정보 페이지 보여주기
    @GetMapping
    public String myPage(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        User loginUser = userService.getLoginUserDetail(user);
        model.addAttribute("loginUser", loginUser);
        return "myPage";
    }

    // 수정 페이지 보여주기
    @GetMapping("/{userId}/edit")
    public String editPage(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable("userId") Long userId, Model model) {
        // 사용자 검증
        User loginUser = userService.validateUser(user, userId);
        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
            return "editPage";
        }
        return "redirect:/";
    }

    @PostMapping("/user/level-image")
    public ResponseEntity<String> getUserLevelImage(@AuthenticationPrincipal CustomOAuth2User user) {
        if (user != null) {
            User loginUser = userService.getLoginUserDetail(user);
            if (loginUser != null) {
                String levelImageName = loginUser.getLevel().getImageName();
                return ResponseEntity.ok(levelImageName);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // 내 정보 수정
    @PostMapping("/{userId}/edit")
    public String editUserInfo(@PathVariable("userId") Long userId, @ModelAttribute EditDTO editDTO) throws IOException {
        userService.editUserProcess(userId, editDTO);
        return "redirect:/myPage";
    }
}