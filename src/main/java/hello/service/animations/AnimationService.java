package hello.service.animations;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.ReviewLikeDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.repository.db.AnimationRepository;
import hello.repository.db.ReviewLikeRepository;
import hello.repository.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AnimationService.class);
    private final ReviewLikeRepository reviewLikeRepository;

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

    // 리뷰 정보 get
    public List<Review> getReviewsByAnimationId(Long id) {
        logger.info("리뷰 최신순으로 받아오기 서비스 로직 실행");
        try {
            List<Review> reviews = reviewRepository.findRecentReviewsByAnimationId(id);
            logger.info("가져온 리뷰 수: {}", reviews.size());
            return reviews;
        } catch (Exception e) {
            logger.error("리뷰 최신순으로 받아오기 중 에러 발생: {}", e.getMessage(), e);
            throw e;
        }
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

    // 메인페이지 애니 별점순 10개
    public List<GetAniListDTO> getTop10AnimationsWithReviewData() {
        return animationRepository.findTopScoreAnimations();
    }
}
