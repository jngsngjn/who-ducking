package hello.controller.notice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notice")
public class NoticePageController {

    @GetMapping
    public String notice() {
        return "/notice/noticePage";
    }
}
