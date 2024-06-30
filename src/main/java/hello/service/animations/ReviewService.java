package hello.service.animations;

import hello.dto.animation.AniReviewDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.review.ReviewLike;
import hello.entity.user.User;
import hello.repository.db.AnimationRepository;
import hello.repository.db.ReviewLikeRepository;
import hello.repository.db.ReviewRepository;
import hello.repository.db.UserRepository;
import hello.service.basic.ExpService;
import hello.service.basic.PointService;
import lombok.RequiredArgsConstructor;
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
    public void addReview(AniReviewDTO aniReviewDTO, User user) {
        Optional<Animation> animationOpt = animationRepository.findById(aniReviewDTO.getAnimationId());
        Optional<User> userOpt = userRepository.findById(aniReviewDTO.getUserId());

        if (animationOpt.isEmpty()) {
            throw new IllegalArgumentException("Animation id: " + aniReviewDTO.getAnimationId() + " not found.");
        }
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User id: " + aniReviewDTO.getUserId() + " not found.");
        }

        Animation animation = animationOpt.get();
        LocalDate today = LocalDate.now();

        long aniReviewCount =  animationRepository.countReviewsByAnimationId(aniReviewDTO.getAnimationId());
        long reviewCountToday = reviewRepository.countReviewByUserAndDate(user, today);

        if (reviewCountToday >= 3) {
            throw new ReviewLimitExceed("하루에 리뷰는 세 번만 작성할 수 있습니다.");
        }

        Review review = new Review();
        review.setAnimation(animation);
        review.setUser(userOpt.get());
        review.setContent(aniReviewDTO.getContent());
        review.setScore(aniReviewDTO.getScore());
        review.setSpoiler(aniReviewDTO.getIsSpoiler());

        reviewRepository.save(review);

        int userReviewCount = user.getReviewCount();
        user.setReviewCount(userReviewCount + 1);

        System.out.println("애니메이션의 리뷰 수는 " + aniReviewCount);
        System.out.println("유저가 작성한  리뷰 수는 " + userReviewCount);

        if (aniReviewCount == 0 || user.getReviewCount() == 0) {
            pointService.increasePoint(user, 5);
            expService.increaseExp(user, 3);
        } else {
            pointService.increasePoint(user, 3);
            expService.increaseExp(user, 3);
        }
    }

    // @ 리뷰 수정
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    // @ 리뷰 삭제 -> (x)
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


    // 리뷰 작성 횟수 제한
    public static class ReviewLimitExceed extends RuntimeException {
        public ReviewLimitExceed(String message) {
            super(message);
        }
    }


//    좋아요 싫어요 기능 구현 6월 29일 밤
// public void likeReview(Long reviewId, Long userId) {
//        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        Optional<ReviewLike> existingReviewLike = reviewLikeRepository.findByReviewIdAndUserId(review, user);
//
//        ReviewLike reviewLike;
//        if (existingReviewLike.isPresent()) {
//            reviewLike = existingReviewLike.get();
//            reviewLike.setIsLike(1);
//            reviewLike.setIsDislike(0);
//        } else {
//            reviewLike = new ReviewLike();
//            reviewLike.setReviewId(review);
//            reviewLike.setUserId(user);
//            reviewLike.setIsLike(1);
//            reviewLike.setIsDislike(0);
//        }
//
//        reviewLikeRepository.save(reviewLike);
//    }
//
//    public void dislikeReview(Long reviewId, User user) {
//
//        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
//        User loginUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 기존 리뷰 좋아요 객체 확인
//        Optional<ReviewLike> existingReviewLike = reviewLikeRepository.findByReviewIdAndUserId(review, loginUser);
//
//        System.out.println("싫어요 클릭시 들어오는 유저 아이디는 " + loginUser.getId());// 잘 들어옴
//
//        // 새로운 리뷰 좋아요 객체 생성 또는 기존 객체 업데이트
//        ReviewLike reviewLike;
//        if (existingReviewLike.isPresent()) {
//            reviewLike = existingReviewLike.get();
//            reviewLike.setIsLike(0);
//            reviewLike.setIsDislike(1);
//        } else {
//            reviewLike = new ReviewLike();
//            reviewLike.setReviewId(review);
//            reviewLike.setUserId(loginUser);
//            reviewLike.setIsLike(0);
//            reviewLike.setIsDislike(1);
//        }
//
//        // 리뷰 좋아요 객체 저장
//        reviewLikeRepository.save(reviewLike);
//    }

    // 좋아요
    @Transactional
    public void isReviewLike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User reviewWriteUser = review.getUser();

        if (!reviewWriteUser.equals(loginUser)) {
            Optional<ReviewLike> existingLikeOpt = reviewLikeRepository.findByReviewIdAndUserId(review, loginUser);
            ReviewLike existingLike = existingLikeOpt.orElse(new ReviewLike());

            existingLike.setUserId(loginUser);
            existingLike.setReviewId(review);
            existingLike.setIsLike(true);
            existingLike.setIsDislike(false);

            reviewLikeRepository.save(existingLike);

            updateLikeAndDislikeCounts(reviewId);
        }
    }


    // 싫어요
    @Transactional
    public void isReviewDisLike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User reviewWriteUser = review.getUser();

        if (!reviewWriteUser.equals(loginUser)) {
            Optional<ReviewLike> existingDislikeOpt = reviewLikeRepository.findByReviewIdAndUserId(review, loginUser);
            ReviewLike existingLike = existingDislikeOpt.orElse(new ReviewLike());

            existingLike.setUserId(loginUser);
            existingLike.setReviewId(review);
            existingLike.setIsLike(false);
            existingLike.setIsDislike(true);

            reviewLikeRepository.save(existingLike);

            updateLikeAndDislikeCounts(reviewId);
        }
    }

    public void updateLikeAndDislikeCounts(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));

        int likeCount = reviewLikeRepository.countReviewLike(review);
        review.setLikeCount(likeCount);

        int dislikeCount = reviewLikeRepository.countReviewDislike(review);
        review.setDislikeCount(dislikeCount);

        reviewRepository.save(review);
    }


}


