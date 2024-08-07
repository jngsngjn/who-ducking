package hello.service.animations;

import hello.dto.animation.AniReviewDTO;
import hello.dto.animation.MyReviewDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.review.ReviewLike;
import hello.entity.user.User;
import hello.exception.ReviewLimitExceedException;
import hello.repository.db.AnimationRepository;
import hello.repository.db.ReviewLikeRepository;
import hello.repository.db.ReviewRepository;
import hello.repository.db.UserRepository;
import hello.service.basic.ExpService;
import hello.service.basic.PointService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AnimationRepository animationRepository;
    private final UserRepository userRepository;
    private final PointService pointService;
    private final ExpService expService;
    private  final ReviewLikeRepository reviewLikeRepository;

    // @ 리뷰 작성
    public void addReview(AniReviewDTO aniReviewDTO, User user, HttpSession session) {
        Optional<Animation> animationOpt = animationRepository.findById(aniReviewDTO.getAnimationId());
        Optional<User> userOpt = userRepository.findById(aniReviewDTO.getUserId());

        if (animationOpt.isEmpty()) {
            throw new IllegalArgumentException("Animation id: " + aniReviewDTO.getAnimationId() + " not found.");
        }
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User id: " + aniReviewDTO.getUserId() + " not found.");
        }

        LocalDate today = LocalDate.now();

        long reviewCountToday = reviewRepository.countReviewByUserAndDate(user, today);
        int currentReviewCount = user.getReviewCount();

        if (reviewCountToday >= 3 || currentReviewCount >= 3) {
            throw new ReviewLimitExceedException("리뷰는 하루에 세 번만 작성 할 수 있습니다.");
        }

        Animation animation = animationOpt.get();

        Review review = new Review();
        review.setAnimation(animation);
        review.setUser(userOpt.get());
        review.setContent(aniReviewDTO.getContent());
        review.setScore(aniReviewDTO.getScore());
        review.setSpoiler(aniReviewDTO.getIsSpoiler());

        user.setReviewCount(currentReviewCount + 1);
        userRepository.save(user);
        reviewRepository.save(review);

        Boolean existReview = animation.getExistReview();
        boolean hasReview = user.isHasReview();

        // 특정 애니의 첫 리뷰 + 계정 첫 리뷰
        if (!existReview && !hasReview) {
            System.out.println("특정 애니의 첫 리뷰 + 계정 첫 리뷰");
            pointService.increasePoint(user, 25); // 시연을 위해 30 포인트에서 25 포인트로 변경
            expService.increaseExp(user, 25, session);
            animation.setExistReview(true);
            user.setHasReview(true);
            userRepository.save(user);
            return;
        }

        // 계정 첫 리뷰일 경우
        if (!hasReview) {
            System.out.println("계정 첫 리뷰");
            pointService.increasePoint(user, 20);
            expService.increaseExp(user, 15, session);
            user.setHasReview(true);
            userRepository.save(user);
            return;
        }

        // 특정 애니 첫 리뷰
        if (!existReview) {
            System.out.println("특정 애니의 첫 리뷰");
            pointService.increasePoint(user, 10);
            expService.increaseExp(user, 10, session);
            animation.setExistReview(true);
            userRepository.save(user);
            return;
        }

        // 일반 리뷰 작성 시
        System.out.println("일반 리뷰");
        pointService.increasePoint(user, 3);
        expService.increaseExp(user, 5, session);
    }

    // @ 리뷰 수정
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    // @ 리뷰 삭제
    public void deleteReview(long reviewId) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);

        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            User user = review.getUser();

            // 리뷰를 전부 지울경우 또다시 5포인트를 받게됨 -> 1.첫 리뷰 boolean을 만든다 vs 2.user가 작성했다는 기록은 남겨두고 이름만 익명으로 바꾼다.
            // user.setReviewCount(user.getReviewCount() - 1);
            //userRepository.save(user);

            reviewRepository.deleteById(reviewId);

        } else {
            System.out.println("Review not found for id: " + reviewId);
        }
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

    public int getReviewCount(Animation animation) {
        return reviewRepository.findReviewCount(animation);
    }

    public Page<MyReviewDTO> getMyReviews(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MyReviewDTO> myReviews = reviewRepository.findMyReviews(user, pageRequest);
        for (MyReviewDTO myReview : myReviews) {
            int reviewCounts = reviewRepository.findReviewCountByAnimationId(myReview.getAnimationId());
            myReview.setTotalComment(reviewCounts);
        }
        return myReviews;
    }

    // 좋아요
    @Transactional
    public void likeReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ReviewLike> existingLikeOpt = reviewLikeRepository.findByReviewIdAndUserId(review, loginUser);

        if (existingLikeOpt.isPresent()) {
            ReviewLike existingLike = existingLikeOpt.get();
            if (existingLike.getIsLike()) {
                reviewLikeRepository.delete(existingLike);
            } else {
                existingLike.setIsLike(true);
                existingLike.setIsDislike(false);
                reviewLikeRepository.save(existingLike);
            }
        } else {
            ReviewLike newLike = new ReviewLike();
            newLike.setUserId(loginUser);
            newLike.setReviewId(review);
            newLike.setIsLike(true);
            newLike.setIsDislike(false);
            reviewLikeRepository.save(newLike);
        }

        updateLikeAndDislikeCounts(reviewId);
    }

    // 싫어요
    @Transactional
    public void dislikeReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ReviewLike> existingDislikeOpt = reviewLikeRepository.findByReviewIdAndUserId(review, loginUser);

        if (existingDislikeOpt.isPresent()) {
            ReviewLike existingDislike = existingDislikeOpt.get();
            if (existingDislike.getIsDislike()) {
                reviewLikeRepository.delete(existingDislike);
            } else {
                existingDislike.setIsLike(false);
                existingDislike.setIsDislike(true);
                reviewLikeRepository.save(existingDislike);
            }
        } else {
            ReviewLike newDislike = new ReviewLike();
            newDislike.setUserId(loginUser);
            newDislike.setReviewId(review);
            newDislike.setIsLike(false);
            newDislike.setIsDislike(true);
            reviewLikeRepository.save(newDislike);
        }

        updateLikeAndDislikeCounts(reviewId);
    }

    // 좋아요 싫어요 업데이트
    public void updateLikeAndDislikeCounts(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));

        int likeCount = reviewLikeRepository.countReviewLike(review);
        review.setLikeCount(likeCount);

        int dislikeCount = reviewLikeRepository.countReviewDislike(review);
        review.setDislikeCount(dislikeCount);

        reviewRepository.save(review);
    }

}