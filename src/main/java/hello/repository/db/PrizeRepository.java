package hello.repository.db;

import hello.dto.admin.AdminPrizeListDTO;
import hello.dto.admin.PrizeDrawDTO;
import hello.dto.playground.prize.PrizeBasicDTO;
import hello.entity.prize.Prize;
import hello.entity.prize.PrizeGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrizeRepository extends JpaRepository<Prize, Long> {

    @Query("SELECT new hello.dto.admin.AdminPrizeListDTO(p.id, p.name, p.startDate, p.endDate, p.grade) " +
            "FROM Prize p WHERE p.endDate > CURRENT_TIMESTAMP ORDER BY p.startDate ASC")
    Page<AdminPrizeListDTO> findCurrentPrizes(Pageable pageable);

    @Query("SELECT new hello.dto.admin.AdminPrizeListDTO(p.id, p.name, p.startDate, p.endDate, p.grade, u.nickname) " +
            "FROM Prize p left JOIN p.user u WHERE p.endDate <= CURRENT_TIMESTAMP ORDER BY p.endDate DESC")
    Page<AdminPrizeListDTO> findExpiredPrizes(Pageable pageable);

    @Query("SELECT new hello.dto.playground.prize.PrizeBasicDTO(p.id, p.name, p.imageName, p.endDate) " +
            "FROM Prize p WHERE p.grade = :grade AND p.endDate > CURRENT_DATE " +
            "ORDER BY p.startDate ASC")
    List<PrizeBasicDTO> findFirstByGradeOrderByStartDateAsc(@Param("grade") PrizeGrade grade, Pageable pageable);

    @Query("SELECT new hello.dto.playground.prize.PrizeBasicDTO(p.id, p.name, p.imageName, p.endDate) " +
            "FROM Prize p where p.grade = :grade  AND p.endDate > CURRENT_DATE ORDER BY p.startDate ASC")
    List<PrizeBasicDTO> findPrizePageByGrade(@Param("grade") PrizeGrade grade);

    @Query("select new hello.dto.admin.PrizeDrawDTO(p.id, p.name) from Prize p " +
            "where p.id = :id")
    PrizeDrawDTO findPrizeDrawById(@Param("id") Long id);
}