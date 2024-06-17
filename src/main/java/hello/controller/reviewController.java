//package hello.controller;
//
//
//import hello.dto.animation.ReviewDTO;
//import hello.service.ReviewService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@Controller
//public class reviewController {
//
//    @Autowired
//    private ReviewService reviewService;
//
//    @PostMapping("/reviewWrite")
//    public String createReview(@RequestBody Model model) {
//        model.addAttribute("formData", new ReviewDTO());
//        return "reviewWrite";
//    }
//}
