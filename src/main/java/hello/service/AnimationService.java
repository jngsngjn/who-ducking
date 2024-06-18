package hello.service;

import hello.dto.animation.GetAniListDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.repository.AnimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnimationService {

    private final AnimationRepository animationRepository;

    public List<GetAniListDTO> getAllAnimationWithReviewData() {
        return animationRepository.findAnimationsWithReviews();
    }

    // 애니 상세 정보 get
    public Animation getAnimationById(Long id) {
        Optional<Animation> animationOptional = animationRepository.findById(id);
        return animationOptional.orElseThrow(() -> new RuntimeException("Animation not found with id " + id));
    }

    // 리뷰 정보 get
    public List<Review> getReviewsByAnimationId(Long animationId) {
        Animation animation = animationRepository.findById(animationId)
                .orElseThrow(() -> new IllegalArgumentException("Animation not found with id: " + animationId));
        return animation.getReviews();
    }
}
