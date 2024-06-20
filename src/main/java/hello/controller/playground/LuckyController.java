package hello.controller.playground;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LuckyController {

    @GetMapping("/lucky")
    public String luckyDraw() {
        return "lucky";
    }
}