package hello.controller.playground;

import hello.dto.playground.QuizDTO;
import hello.service.playground.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/playground/quiz")
    public String quiz() {
        return "playground/quiz";
    }

    @ResponseBody
    @GetMapping("/api/quizzes")
    public List<QuizDTO> getQuizzes() {
        return quizService.getRandomQuizzes();
    }
}