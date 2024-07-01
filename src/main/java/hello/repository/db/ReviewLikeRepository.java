package hello.repository.db;

import hello.entity.review.Review;
import hello.entity.review.ReviewLike;
import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndUserId(Review review, User user);

    // 리뷰의 좋아요
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.reviewId = :review AND rl.isLike = true")
    int countReviewLike(@Param("review") Review review);

    // 리뷰의 싫어요
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.reviewId = :review AND rl.isDislike = true")
    int countReviewDislike(@Param("review") Review review);

    // 리뷰에 좋아요를 누를 사람을 알아내야해...
    @Query("SELECT rl FROM ReviewLike rl WHERE rl.userId = :user AND rl.reviewId = :review")
    List<ReviewLike> findByUserIdAndReviewId(@Param("user") User userId, @Param("review") Review reviewId);



    // 좋아요 해내고 만다......
//    List<ReviewLike> findByReviewId(Long reviewId); 아닌거같아 anirepo같아..
}


