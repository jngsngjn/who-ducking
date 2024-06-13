package hello.repository.db;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.GetOneAniDTO;
import hello.entity.animation.Animation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//Animation 엔티티를 다루는 JpaRepository를 상속, Long은 Animation id의 타입인듯
public interface AnimationRepository extends JpaRepository<Animation, Long> {

//    // 애니 전체 리스트 조회
//    List <GetAniListDTO> getAniLists();
//
//    // 애니 상제 데이터 가져오기
//    @Query("SELECT a FROM Animation a WHERE a.id= :id")
//        GetOneAniDTO getAniInfo(Long id);

//    @Query("SELECT new hello.dto.animation.GetOneAniDTO() FROM Animation ani join ani.")

}