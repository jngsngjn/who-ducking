package hello.controller.animations;

import hello.dto.animation.AniReviewDTO;
import hello.entity.review.Review;
import hello.repository.db.ReviewRepository;
import hello.service.animations.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    /* POST : 리뷰 작성 */
    @PostMapping("/animations/{AnimationId}/review")
    public String writeReview (@PathVariable long AnimationId, @ModelAttribute AniReviewDTO aniReviewDTO){
        aniReviewDTO.setAnimationId(AnimationId);
        reviewService.addReview(aniReviewDTO);
        return "redirect:/animations/" + AnimationId;
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

    /* @ 좋아요 클릭시 요청 */
    @PatchMapping("/reviews/{id}/like")
    public ResponseEntity<?> likeReview(@PathVariable("id") Long id) {
        Review reviewId = reviewRepository.findById(id).orElse(null);
        if (reviewId == null) {
            return ResponseEntity.notFound().build();
        }
        reviewId.setLikeCount(reviewId.getLikeCount() + 1);
        reviewRepository.save(reviewId);

        return ResponseEntity.ok().body(reviewId.getLikeCount());
    }


    /* @ 싫어요 클릭시 요청 */
    @PatchMapping("/reviews/{id}/dislike")
    public ResponseEntity<?> dislikeReview(@PathVariable("id") Long id) {
        Review reviewId = reviewRepository.findById(id).orElse(null);
        if (reviewId == null) {
            return ResponseEntity.notFound().build();
        }
        reviewId.setDislikeCount(reviewId.getDislikeCount() + 1);
        reviewRepository.save(reviewId);

        return ResponseEntity.ok().body(reviewId.getDislikeCount());
    }
}
