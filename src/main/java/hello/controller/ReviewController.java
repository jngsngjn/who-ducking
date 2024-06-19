package hello.controller;

import hello.dto.animation.AniReviewDTO;
import hello.entity.review.Review;
import hello.service.AnimationService;
import hello.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AnimationService animationService;

    /* POST : 리뷰 작성 */
    @PostMapping("/community/{AnimationId}/review")
    public String writeReview (@PathVariable long AnimationId, @ModelAttribute AniReviewDTO aniReviewDTO){
        aniReviewDTO.setAnimationId(AnimationId);
        reviewService.addReview(aniReviewDTO);
        return "redirect:/community/" + AnimationId;
    }

    /* @ 리뷰 수정
    *  @ id = reviewId
    * */
//    @PatchMapping("/reviews/patch/{id}")
//    public String updateReview (@PathVariable long id, @RequestBody Review review){
//        review.setId(id);
//    }

    /* @ 리뷰 삭제
    *  @ id = reviewId */
    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
