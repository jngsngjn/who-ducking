package hello.repository.db;

import hello.entity.user.LoginHistory;
import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    Optional<LoginHistory> findByUserAndLoginDate(User user, LocalDate loginDate);
    boolean existsByUserAndLoginDate(User user, LocalDate loginDate);
    List<LoginHistory> findAllByUser(User user);
}