package hello.repository.db;

import hello.entity.prize.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    Optional<Entry> findByUserId(Long id);

    @Query("select e from Entry e where e.user.id = :userId and e.prize.id = :prizeId")
    Optional<Entry> findByUserIdAndPrizeId(@Param("userId") Long userId, @Param("prizeId") Long prizeId);

    List<Entry> findByPrizeId(Long prizeId);
}