package hello.controller.playground;

import hello.service.playground.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/quiz")
    public String quiz(Model model) {
        model.addAttribute("quizzes");
        return "playground/quiz";
    }
}