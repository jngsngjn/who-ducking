package hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewWriteController {
    @GetMapping("/review-write")
    public String reviewWritePage(){
        return "reviewWrite";
    }
}
