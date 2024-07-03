package hello.service.animations;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.ReviewLikeDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.repository.db.AnimationRepository;
import hello.repository.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnimationService {

    private final AnimationRepository animationRepository;
    private final ReviewRepository reviewRepository;

    // 모든 애니 get
    public List<GetAniListDTO> getAllAnimationWithReviewData() {
        return animationRepository.findAnimationsWithReviews();
    }

    // 애니 상세 정보 get
    public Animation getAnimationById(Long id) {
        Optional<Animation> animationOptional = animationRepository.findById(id);
        Animation animation = animationRepository.findById(id).get();

        Hibernate.initialize(animation.getAnimationGenres());// 애니랑 애니장르가 연관관계인데 가져올때 지연로딩 세팅이 되어있어서 불러오려니까 못 불러옴 -> 강제로 프록시 초기화하니까 타임리프 에러가 안남
        Hibernate.initialize(animation.getReviews());

        return animationOptional.orElseThrow(() -> new RuntimeException("Animation not found with id " + id));
    }

    // 애니 상세페이지의  리뷰수 & 평점 조회
    public List<GetAniListDTO> getCountReviewAndScore(Long id){
        return animationRepository.findAnimationDetailsById(id);
    }

    // 최신순으로 정렬된 리뷰 get
    public List<Review> getRecentReviewsByAnimationId(Long animationId) {
        return reviewRepository.findRecentReviewsByAnimationId(animationId);
    }

    // 인기순으로 정렬된 리뷰 get
    public List<Review> getTopReviewsByAnimationId(Long animationId) {
        return reviewRepository.findTopReviewsByAnimationId(animationId);
    }

    // 좋아요 여부 확인
    public List<ReviewLikeDTO> getReviewLikesByAnimationId(Long animationId) {
        return animationRepository.findReviewLikesByAnimationId(animationId);
    }
}