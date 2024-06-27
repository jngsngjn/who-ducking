package hello.controller.mypage;

import hello.dto.user.AccountDeletionDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.account.EmailService;
import hello.service.basic.UserService;
import hello.service.account.AccountService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class AccountDeletionController {

    private final UserService userService;
    private final EmailService emailService;
    private final AccountService accountService;

    // 회원 탈퇴 페이지
    @GetMapping("/account-deletion")
    public String deletePage(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        User loginUser = userService.getLoginUserDetail(user);
        model.addAttribute("email", loginUser.getEmail());
        return "mypage/accountDeletion";
    }

    // 계정 삭제 인증 코드 이메일 전송
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<Void> sendVerificationCode(@RequestParam("email") String email) throws MessagingException, IOException, URISyntaxException {
        emailService.sendVerificationCode(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 클라이언트 측 코드 검증
    @ResponseBody
    @PostMapping("/account-deletion/verifyCode-ajax")
    public boolean verifyCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        return emailService.verifyCode(email, code);
    }

    // 계정 삭제
    @PostMapping("/account-deletion/verifyCode")
    public String deleteAccount(@ModelAttribute AccountDeletionDTO accountDeletionDTO) {
        accountService.deleteAccountProcess(accountDeletionDTO);
        return "redirect:/logout";
    }
}