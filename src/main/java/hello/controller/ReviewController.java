package hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReviewController {

    @PostMapping("/postReview")
    public ModelAndView postReview(
            @RequestParam("reviewContent") String reviewContent,
            @RequestParam(value = "spoilerIncluded", required = false) boolean spoilerIncluded
    ) {
    // 일단 아이디를 가져와야해..
        ModelAndView modelAndView = new ModelAndView("forward:/currentUrl");
        return modelAndView;
    }
}
