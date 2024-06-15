package hello.repository.db;

import hello.dto.animation.GetAniListDTO;
import hello.entity.animation.Animation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnimationRepository extends JpaRepository<Animation, Long> {

    // 애니 전체 리스트 조회


    @Query("SELECT new hello.dto.animation.GetAniListDTO(a.id, a.imagePath, COALESCE(AVG(r.score), 0.0), COUNT(r.id)) " +
            "FROM Animation a LEFT JOIN a.reviews r " +
            "GROUP BY a.id, a.imagePath")
    List<GetAniListDTO> findAnimationsWithReviews();


    Animation findById(long id);
}
