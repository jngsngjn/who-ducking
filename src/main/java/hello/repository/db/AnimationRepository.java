package hello.repository.db;


import hello.dto.animation.GetAniListDTO;
import hello.dto.playground.WorldCupDTO;
import hello.entity.animation.Animation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnimationRepository extends JpaRepository<Animation, Long> {

    // 애니 전체 리스트 조회 -> 최신순으로 조회 + 장르 id 추가
    @Query("SELECT new hello.dto.animation.GetAniListDTO(a.id, a.imageName, ROUND(COALESCE(AVG(r.score), 0.0), 1), COUNT(DISTINCT r.id), a.name, " +
            "MIN(ag.genre.id), MAX(ag.genre.id)) " +
            "FROM Animation a " +
            "LEFT JOIN a.reviews r " +
            "LEFT JOIN a.animationGenres ag " +
            "GROUP BY a.id, a.imageName, a.name " +
            "ORDER BY a.id DESC")
    List<GetAniListDTO> findAnimationsWithReviews();


    // 애니상세리스트 별 평점과 리뷰수 조회
    @Query("SELECT new hello.dto.animation.GetAniListDTO(a.id, NULL, ROUND(COALESCE(AVG(r.score), 0.0), 1), COUNT(r.id), a.name) " +
            "FROM Animation a LEFT JOIN a.reviews r " +
            "WHERE a.id = :animationId " +
            "GROUP BY a.id, a.name")
    List<GetAniListDTO> findAnimationDetailsById(long animationId);


    @Query("SELECT new hello.dto.playground.WorldCupDTO(a.id, a.name, a.imageName) " +
            "FROM Animation a " +
            "ORDER BY a.id DESC")
    List<WorldCupDTO> findWorldCupAnimations();

    @Query(value = "SELECT a.id, a.name AS answer, a.image_name AS imageName FROM Animation a ORDER BY RAND()", nativeQuery = true)
    List<Object[]> findRandomQuizzes();
}