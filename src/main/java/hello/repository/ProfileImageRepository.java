package hello.repository;

import hello.entity.user.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    void deleteByUserId(Long id);

    ProfileImage findByUserId(Long id);
}