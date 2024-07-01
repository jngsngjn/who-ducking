package hello.controller.playground;

import hello.dto.playground.QuizDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.basic.PointService;
import hello.service.basic.UserService;
import hello.service.playground.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;
    private final PointService pointService;

    @GetMapping("/playground/quiz")
    public String quiz() {
        return "playground/quiz";
    }

    @ResponseBody
    @GetMapping("/api/quizzes")
    public List<QuizDTO> getQuizzes() {
        return quizService.getRandomQuizzes();
    }

    @ResponseBody
    @PostMapping("/playground/quiz/perfect")
    public void perfectScore(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        User loginUser = userService.getLoginUserDetail(oAuth2User);
        pointService.increasePoint(loginUser, 15);
    }
}