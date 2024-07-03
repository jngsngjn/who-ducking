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
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping("/playground/quiz/result")
    public void earnPoints(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                           @RequestParam("correctCount") int correctCount) {
        User loginUser = userService.getLoginUserDetail(oAuth2User);

        switch (correctCount) {
            case 1 : pointService.increasePoint(loginUser, 3);
                System.out.println("3포인트 지급");
                break;
            case 2 : pointService.increasePoint(loginUser, 6);
                System.out.println("6포인트 지급");
                break;
            case 3 : pointService.increasePoint(loginUser, 9);
                System.out.println("9포인트 지급");
                break;
            case 4 : pointService.increasePoint(loginUser, 12);
                System.out.println("12포인트 지급");
                break;
            default: pointService.increasePoint(loginUser, 15);
                System.out.println("15포인트 지급");
        }
    }
}