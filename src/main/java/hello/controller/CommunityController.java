package hello.controller;

import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.repository.AnimationRepository;
import hello.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;


@Controller
public class CommunityController {

    private final AnimationService animationService;


    @Autowired
    public CommunityController(AnimationService animationService) {
        this.animationService = animationService;
    }

    // 전체 애니메이션 데이터 조회
    @GetMapping("/community")
    public String getAllAnimationsWithReviewData(Model model) {
        model.addAttribute("animationLists", animationService.getAllAnimationWithReviewData());

        return "reviewBoard";
    }

    // 애니메이션 상세페이지 GET(리뷰작성페이지)
    @GetMapping("community/{id}")
    public String getAnimation(@PathVariable Long id, Model model) {
        Animation animation = animationService.getAnimationById(id);
        model.addAttribute("aniDetailInfo", animation);

        List<Review> reviews = animationService.getReviewsByAnimationId(id);
        model.addAttribute("reviews", reviews);

        return "reviewWrite";
    }

//    @GetMapping("community/{id}")
//    public String getReviewsByAnimationId(@PathVariable Long animationId, Model model) {
//        List <Review> reviews = animationService.getReviewsByAnimationId(animationId);
//        model.addAttribute("reviews", reviews);
//        return "reviewWrite";
//    }
}