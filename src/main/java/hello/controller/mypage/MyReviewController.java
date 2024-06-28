package hello.controller.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyReviewController {

    @GetMapping("/myPage/reviews")
    public String myReviews() {
        return "mypage/myReview";
    }
}