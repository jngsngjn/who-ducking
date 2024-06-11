package hello.controller;

import hello.dto.oauth2.OAuth2Response;
import hello.dto.user.RegisterBasicDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

        if (oauth2Response == null) {
            return "redirect:/";
        }

        String email = oauth2Response.getEmail();
        model.addAttribute("email", email);

        session.setAttribute("registering", true);
        return "registerBasic";
    }

    // AJAX
    @ResponseBody
    @PostMapping("/check-duplicate-nickname")
    public boolean checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        return !registerService.isDuplicateNickname(nickname);
    }

    // AJAX
    @ResponseBody
    @PostMapping("/send-code")
    public Map<String, Object> sendCode(@RequestBody String phone) {
        Map<String, Object> result = registerService.isDuplicatePhone(phone);

        if (!((boolean) result.get("isDuplicate"))) {
            smsService.sendCode(phone);
        }

        return result;
    }

    // AJAX
    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody SmsCodeVerificationRequest request) {
        boolean isValid = codeStore.verifyCode(request.getPhone(), request.getCode());
        if (isValid) {
            codeStore.removeCode(request.getPhone());
        }
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @PostMapping("/check-recommender")
    public ResponseEntity<Boolean> checkRecommender(@RequestBody String referrer) {
        boolean result = registerService.recommenderCheck(referrer);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/genre")
    public String genrePage(HttpSession session, @Validated @ModelAttribute RegisterBasicDTO registerBasicDTO,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registerBasic";
        }

        session.setAttribute("registerBasicDTO", registerBasicDTO);
        return "redirect:/register/genre";
    }

    @GetMapping("/genre")
    public String genrePage(HttpSession session) {
        Boolean registering = (Boolean) session.getAttribute("registering");
        if (registering == null || !registering) {
            return "redirect:/";
        }

        return "registerGenre";
    }

    @PostMapping
    public String registerGenre(HttpSession session, @RequestParam("genres") List<String> genres) {

        if (genres.size() > 5) {
            return "registerGenre";
        }

        OAuth2Response oauth2Response = (OAuth2Response) session.getAttribute("oauth2Response");
        RegisterBasicDTO registerBasicDTO = (RegisterBasicDTO) session.getAttribute("registerBasicDTO");

        registerService.registerProcess(oauth2Response, registerBasicDTO, genres);

        // 세션 삭제
        session.removeAttribute("oauth2Response");
        session.removeAttribute("registerBasicDTO");
        session.removeAttribute("registering");
        return "redirect:/";
    }
}