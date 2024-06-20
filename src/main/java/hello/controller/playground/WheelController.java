package hello.controller.playground;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WheelController {
    @GetMapping("/wheel")
    public String wheelPage(){
        return "wheel";
    }
}
