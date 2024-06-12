package hello.service;

import hello.dto.user.CustomOAuth2User;
import hello.dto.user.EditDTO;
import hello.entity.user.Image;
import hello.entity.user.ProfileImage;
import hello.entity.user.User;
import hello.repository.FileStore;
import hello.repository.ProfileImageRepository;
import hello.repository.UserRepository;
import hello.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final FileStore fileStore;

    public User findUserServiceById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User getLoginUserDetail(CustomOAuth2User user) {
        User loginUser = userRepository.findByUsername(user.getUsername());
        Hibernate.initialize(loginUser.getUserGenres());
        return loginUser;
    }

    public void editUserProcess(Long id, EditDTO editDTO) throws IOException {
        User findUser = findUserServiceById(id);
        findUser.setEmailConsent(editDTO.isEmailConsent());
        findUser.setGender(editDTO.getGender());

        MultipartFile profileImage = editDTO.getProfileImage();

        // 사용자가 사진을 업로드한 경우
        if (profileImage != null && !profileImage.isEmpty()) {
            // 이미 기존 사진이 있다면 삭제
            ProfileImage currentImage = findUser.getProfileImage();
            if (currentImage != null) {
                findUser.setProfileImage(null);
                profileImageRepository.deleteByUserId(id);
                fileStore.deleteFile(currentImage.getStoreImageName());
            }

            Image image = fileStore.storeFile(profileImage); // 서버에 이미지 저장

            ProfileImage newProfileImage = new ProfileImage(image.getStoreImageName(), image.getImagePath(), findUser);
            findUser.setProfileImage(newProfileImage);
        }
    }

    public User validateUser(CustomOAuth2User user, Long id) {
        User loginUser = getLoginUserDetail(user);
        if (loginUser != null && loginUser.getId().equals(id)) {
            return loginUser;
        }
        return null;
    }
}