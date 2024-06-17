package hello.repository;

import hello.entity.user.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

    EmailCode findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmailCode e WHERE e.createdAt < :expiryTime")
    void deleteExpiredCodes(@Param("expiryTime") LocalDateTime expiryTime);
}