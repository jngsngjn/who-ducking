package hello.service.basic;

import hello.dto.user.CustomOAuth2User;
import hello.dto.user.UserInfoEditDTO;
import hello.entity.genre.Genre;
import hello.entity.genre.UserGenre;
import hello.entity.user.Address;
import hello.entity.user.Image;
import hello.entity.user.ProfileImage;
import hello.entity.user.User;
import hello.repository.db.*;
import hello.repository.server.FileStore;
import hello.exception.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final FileStore fileStore;
    private final GenreRepository genreRepository;
    private final UserGenreRepository userGenreRepository;

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User getLoginUserDetail(CustomOAuth2User user) {
        User loginUser = userRepository.findByUsername(user.getUsername());
        Hibernate.initialize(loginUser.getUserGenres());
        return loginUser;
    }

    // 사용자 정보 업데이트
    public void editUserProcess(Long id, UserInfoEditDTO userInfoEditDTO, HttpSession session) throws IOException {
        User findUser = findUserById(id);
        findUser.setEmailConsent(userInfoEditDTO.isEmailConsent());
        findUser.setGender(userInfoEditDTO.getGender());
        findUser.setNickname(userInfoEditDTO.getNickname());
        session.setAttribute("nickname", userInfoEditDTO.getNickname());
        findUser.setPhone(userInfoEditDTO.getPhone());
        findUser.setHomeAddress(new Address(userInfoEditDTO.getZipcode(), userInfoEditDTO.getAddress(), userInfoEditDTO.getDetailAddress()));

        // 장르 설정
        userGenreRepository.deleteByUser(id);
        List<String> genres = userInfoEditDTO.getSelectedGenres();
        List<Genre> allGenres = genreRepository.findByNameIn(genres);

        allGenres.forEach(genre -> {
            UserGenre userGenre = new UserGenre(findUser, genre);
            findUser.getUserGenres().add(userGenre);
        });

        MultipartFile uploadImage = userInfoEditDTO.getProfileImage();
        ProfileImage currentImage = findUser.getProfileImage();

        // 사용자가 사진을 업로드한 경우
        if (uploadImage != null && !uploadImage.isEmpty()) {
            // 이미 기존 사진이 있다면 삭제
            if (currentImage != null) {
                clearProfileImage(id, findUser, currentImage);
            }

            Image image = fileStore.storeProfileImageFile(uploadImage); // 서버에 이미지 저장

            ProfileImage newProfileImage = new ProfileImage(image.getStoreImageName(), image.getImagePath(), findUser);
            findUser.setProfileImage(newProfileImage);

            session.setAttribute("profileImageName", newProfileImage.getStoreImageName());
        }

        // 기본 이미지 사용 버튼을 클릭한 경우
        boolean useDefaultImage = userInfoEditDTO.isUseDefaultImage();
        if (useDefaultImage && currentImage != null) {
            clearProfileImage(id, findUser, currentImage);
            session.setAttribute("profileImageName", null);
        }
    }

    private void clearProfileImage(Long id, User findUser, ProfileImage currentImage) {
        findUser.setProfileImage(null);
        profileImageRepository.deleteByUserId(id);
        fileStore.deleteProfileImage(currentImage.getStoreImageName());
    }

    public User validateUser(CustomOAuth2User user, Long id) {
        User loginUser = getLoginUserDetail(user);
        if (loginUser != null && loginUser.getId().equals(id)) {
            return loginUser;
        }
        return null;
    }

    public boolean isLevelOne(User user) {
        Long level = user.getLevel().getId();
        return level.equals(1L);
    }
}