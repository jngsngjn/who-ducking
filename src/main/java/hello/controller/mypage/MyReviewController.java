package hello.controller.mypage;

import hello.dto.animation.MyReviewDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.animations.ReviewService;
import hello.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MyReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/myPage/reviews")
    public String myReviews(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            Model model) {
        User loginUser = userService.getLoginUserDetail(oAuth2User);
        Page<MyReviewDTO> myReviews = reviewService.getMyReviews(loginUser, page, 12);
        model.addAttribute("myReviews", myReviews);
        return "mypage/myReview";
    }
}