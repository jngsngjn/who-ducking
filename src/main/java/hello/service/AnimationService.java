package hello.service;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.GetAniDetailDTO;
import hello.dto.animation.ReviewDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.repository.AnimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnimationService {

    private final AnimationRepository animationRepository;

    public List<GetAniListDTO> getAllAnimationWithReviewData() {
        return animationRepository.findAnimationsWithReviews();
    }

    // 애니 상세 정보 get
    public GetAniDetailDTO getAnimationDetailById(Long animationId) {
        Optional<Animation> animationOptional = animationRepository.findById(animationId);
        Animation animation = animationOptional.orElseThrow(() ->
                new IllegalArgumentException("Animation with id " + animationId + " not found"));

        return new GetAniDetailDTO(animation.getId(), animation.getName(), animation.getAuthor(),
                animation.getDescription(), animation.getFirstDate(), animation.getImagePath(),
                animation.getIsFinished(), animation.getRating());
    }

    private ReviewDTO convertToReviewDto(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setUsername(review.getUser().getUsername());
        dto.setLikeCount(review.getLikeCount());
        dto.setDislikeCount(review.getDislikeCount());
        dto.setWriteDate(review.getWriteDate());
        dto.setScore(review.getScore());
        dto.setContent(review.getContent());
        return dto;
    }
}
