package hello.repository;

import hello.dto.admin.UserInfoDTO;
import hello.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByNickname(String nickname);

    User findByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT u.phone FROM User u")
    List<String> findAllPhoneNumbers();

    @Query("SELECT u.socialType FROM User u WHERE u.phone = :phone")
    String findSocialTypeByPhone(@Param("phone") String phone);

    void deleteByEmail(String email);

    @Query("SELECT new hello.dto.admin.UserInfoDTO(u.socialType, u.nickname, u.gender, u.email, u.level.id, u.joinDate) FROM User u")
    Page<UserInfoDTO> findUserInfo(Pageable pageable);
}