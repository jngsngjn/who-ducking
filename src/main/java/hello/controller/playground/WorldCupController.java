package hello.controller.playground;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WorldCupController {

    @GetMapping("/world")
    public String world() {
        return "world";
    }
}