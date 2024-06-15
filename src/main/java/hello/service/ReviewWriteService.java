package hello.service;

import hello.dto.animation.ReviewDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.user.User;
import hello.repository.AnimationRepository;
import hello.repository.ReviewRepository;
import hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewWriteService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AnimationRepository animationRepository;

    @Autowired
    public ReviewWriteService(ReviewRepository reviewRepository, UserRepository userRepository,
                              AnimationRepository animationRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.animationRepository = animationRepository;
    }

    @Transactional
    public Long saveReview(ReviewDTO reviewDTO) {
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + reviewDTO.getUserId()));

        Animation animation = animationRepository.findById(reviewDTO.getAnimationId())
                .orElseThrow(() -> new IllegalArgumentException("Animation not found with id: " + reviewDTO.getAnimationId()));

        Review review = new Review();
        review.setUser(user);
        review.setAnimation(animation);
        review.setContent(reviewDTO.getContent());
        review.setScore(reviewDTO.getScore());
        review.setWriteDate(LocalDateTime.now());
        review.setLikeCount(0);
        review.setDislikeCount(0);

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }
}
