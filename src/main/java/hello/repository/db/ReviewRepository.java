package hello.repository.db;

import hello.entity.animation.Animation;
import hello.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /* @인기순 리뷰들 불러오기 */
    @Query("SELECT r FROM Review r WHERE r.animation.id = :animationId ORDER BY r.likeCount DESC")
    List<Review> findTopReviewsByAnimationId(@Param("animationId") Long animationId);

    /* @ 최근 게시글 불러오기 -> (O) */
    @Query("SELECT r FROM Review r WHERE r.animation.id = :animationId ORDER BY r.writeDate DESC")
    List<Review> findRecentReviewsByAnimationId(@Param("animationId") Long animationId);

    @Query("select count(r) from Review r where r.animation = :animation")
    int findReviewCount(@Param("animation") Animation animation);
}