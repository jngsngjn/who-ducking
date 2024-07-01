package hello.repository.db;

import hello.dto.animation.MyReviewDTO;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /* @ 애니메이션 리뷰 개수 조회 */
    @Query("select count(r) from Review r where r.animation = :animation")
    int findReviewCount(@Param("animation") Animation animation);

    // 애니메이션 리뷰 개수 조회
    @Query("select count(r) from Review r where r.animation.id =:id")
    int findReviewCountByAnimationId(@Param("id") Long id);

    // 내가 쓴 리뷰 모두 조회
    @Query("select new hello.dto.animation.MyReviewDTO(r.animation.id, r.animation.name, r.animation.imageName, r.score) from Review r" +
            " where r.user = :user order by r.writeDate")
    Page<MyReviewDTO> findMyReviews(@Param("user") User user, Pageable pageable);
}