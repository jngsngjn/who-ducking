package hello.service.animations;

import hello.dto.animation.AniReviewDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.user.User;
import hello.repository.db.AnimationRepository;
import hello.repository.db.ReviewRepository;
import hello.repository.db.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AnimationRepository animationRepository;
    private final UserRepository userRepository;

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

    // 매일 자정 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetReviewCount() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setReviewCount(0);
        }
        userRepository.saveAll(users);
    }
}