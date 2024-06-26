package hello.controller.basic;

import hello.dto.oauth2.OAuth2Response;
import hello.dto.user.RegisterBasicDTO;
import hello.dto.user.SmsCodeVerificationRequest;
import hello.repository.memory.CodeStore;
import hello.service.register.RegisterService;
import hello.service.basic.SmsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final SmsService smsService;
    private final RegisterService registerService;
    private final CodeStore codeStore;

    private static final int MAX_GENRES = 5;

    @GetMapping("/basic")
    public String registerBasicForm(HttpSession session, Model model) {
        OAuth2Response oauth2Response = (OAuth2Response) session.getAttribute("oauth2Response");

        if (oauth2Response == null) {
            return "redirect:/";
        }

        String email = oauth2Response.getEmail();
        model.addAttribute("email", email);

        session.setAttribute("registering", true);
        return "basic/registerBasic";
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
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return "basic/registerBasic";
        }

        session.setAttribute("registerBasicDTO", registerBasicDTO);
        session.setAttribute("registerBasic", true);
        return "redirect:/register/genre";
    }

    @GetMapping("/genre")
    public String genrePage(HttpSession session) {
        Boolean registering = (Boolean) session.getAttribute("registering");
        Boolean registerBasic = (Boolean) session.getAttribute("registerBasic");
        if (registering == null || !registering || registerBasic == null || !registerBasic) {
            return "redirect:/";
        }

        return "basic/registerGenre";
    }

    @PostMapping
    public String registerGenre(HttpSession session, @RequestParam("genres") List<String> genres,
                                RedirectAttributes redirectAttributes) {

        if (genres.size() > MAX_GENRES) {
            return "basic/registerGenre";
        }

        OAuth2Response oauth2Response = (OAuth2Response) session.getAttribute("oauth2Response");
        RegisterBasicDTO registerBasicDTO = (RegisterBasicDTO) session.getAttribute("registerBasicDTO");

        registerService.registerProcess(oauth2Response, registerBasicDTO, genres);

        // 세션 삭제
        session.removeAttribute("oauth2Response");
        session.removeAttribute("registerBasicDTO");
        session.removeAttribute("registering");
        session.removeAttribute("registerBasic");
        redirectAttributes.addFlashAttribute("welcomeMessage", "회원가입을 환영합니다!");
        return "redirect:/login";
    }
}