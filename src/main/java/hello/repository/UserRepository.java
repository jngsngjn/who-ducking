package hello.repository;

import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    User findByEmail(String email);
}