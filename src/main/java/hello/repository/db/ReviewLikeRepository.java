package hello.repository.db;

import hello.entity.review.Review;
import hello.entity.review.ReviewLike;
import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndUserId(Review review, User user);

    // 리뷰의 좋아요 개수 가져오기
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.reviewId = :review AND rl.isLike = true")
    int countReviewLike(@Param("review") Review review);

    // 리뷰의 싫어요 개수 가져오기
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.reviewId = :review AND rl.isDislike = true")
    int countReviewDislike(@Param("review") Review review);
}


