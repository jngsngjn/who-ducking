package hello.repository.db;

import hello.entity.review.Review;
import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /* @인기순 리뷰들 불러오기 */
    @Query("SELECT r FROM Review r WHERE r.animation.id = :animationId ORDER BY r.likeCount DESC")
    List<Review> findTopReviewsByAnimationId(@Param("animationId") Long animationId);

    /* @ 최근 게시글 불러오기 */
    @Query("SELECT r FROM Review r WHERE r.animation.id = :animationId ORDER BY r.writeDate DESC")
    List<Review> findRecentReviewsByAnimationId(@Param("animationId") Long animationId);

    /* @ 당일 작성 리뷰 개수 조회 */
    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user AND DATE(r.writeDate) = :date")
    long countReviewByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

}

