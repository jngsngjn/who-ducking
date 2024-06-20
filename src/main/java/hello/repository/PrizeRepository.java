package hello.repository;

import hello.dto.admin.PrizeListDTO;
import hello.entity.prize.Prize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrizeRepository extends JpaRepository<Prize, Long> {

    @Query("SELECT new hello.dto.admin.PrizeListDTO(p.id, p.name, p.startDate, p.endDateTime, p.grade) " +
            "FROM Prize p WHERE p.endDateTime > CURRENT_TIMESTAMP ORDER BY p.startDate ASC")
    Page<PrizeListDTO> findCurrentPrizes(Pageable pageable);

    @Query("SELECT new hello.dto.admin.PrizeListDTO(p.id, p.name, p.startDate, p.endDateTime, p.grade, u.nickname) " +
            "FROM Prize p left JOIN p.user u WHERE p.endDateTime <= CURRENT_TIMESTAMP ORDER BY p.endDateTime DESC")
    Page<PrizeListDTO> findExpiredPrizes(Pageable pageable);
}