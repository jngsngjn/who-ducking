package hello.repository;

import hello.entity.prize.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    Optional<Entry> findByUserId(Long id);

    List<Entry> findByPrizeId(Long prizeId);
}