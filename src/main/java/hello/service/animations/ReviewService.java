package hello.service.animations;

import hello.dto.animation.AniReviewDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.user.User;
import hello.repository.db.AnimationRepository;
import hello.repository.db.ReviewRepository;
import hello.repository.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private AnimationRepository animationRepository;
    @Autowired
    private UserRepository userRepository;

    public void addReview(AniReviewDTO aniReviewDTO) {

        Optional<Animation> animationOpt = animationRepository.findById(aniReviewDTO.getAnimationId());
        Optional<User> userOpt = userRepository.findById(aniReviewDTO.getUserId());

        if (animationOpt.isPresent() && userOpt.isPresent()) {
            Review review = new Review();
            review.setAnimation(animationOpt.get());
            review.setUser(userOpt.get());
            review.setContent(aniReviewDTO.getContent());
            review.setScore(aniReviewDTO.getScore());
            review.setSpoiler(aniReviewDTO.getIsSpoiler());

            reviewRepository.save(review);

        } else {
            System.out.println("Animation or User not found");
            if (animationOpt.isEmpty()) {
                System.out.println("애니메이션 id : " + aniReviewDTO.getAnimationId());
            }
            if (userOpt.isEmpty()) {
                System.out.println("유저 id가: " + aniReviewDTO.getUserId());
            }
        }
    }

    // @ 리뷰 삭제
    public void deleteReview(long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // 좋아요 싫어요 2

}


