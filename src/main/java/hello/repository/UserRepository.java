package hello.repository;

import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    @Query("SELECT u.phone FROM User u")
    List<String> findAllPhoneNumbers();

    @Query("SELECT u.socialType FROM User u WHERE u.phone = :phone")
    String findSocialTypeByPhone(@Param("phone") String phone);

}