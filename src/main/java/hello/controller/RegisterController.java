package hello.controller;

import hello.dto.oauth2.OAuth2Response;
import hello.dto.user.RegisterDTO;
import hello.dto.user.SmsCodeVerificationRequest;
import hello.repository.CodeStore;
import hello.service.RegisterService;
import hello.service.SmsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final SmsService smsService;
    private final RegisterService registerService;
    private final CodeStore codeStore;

    @GetMapping("/basic")
    public String registerBasicForm(HttpSession session, Model model) {
        OAuth2Response oauth2Response = (OAuth2Response) session.getAttribute("oauth2Response");
        String email = oauth2Response.getEmail();
        model.addAttribute("email", email);
        return "registerBasic";
    }

    // AJAX
    @ResponseBody
    @PostMapping("/check-duplicate-nickname")
    public boolean checkDuplicateId(@RequestParam("nickname") String nickname) {
        return !registerService.isDuplicateNickname(nickname);
    }

    // AJAX
    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestBody String phone) {
        smsService.sendCode(phone);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // AJAX
    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody SmsCodeVerificationRequest request) {
        boolean isValid = codeStore.verifyCode(request.getPhone(), request.getCode());
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @PostMapping
    public String register(HttpSession session, @ModelAttribute RegisterDTO registerDTO) {
        OAuth2Response oauth2Response = (OAuth2Response) session.getAttribute("oauth2Response");
        registerService.registerProcess(oauth2Response, registerDTO);
        return "redirect:/";
    }
}