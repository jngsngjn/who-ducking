package hello.service;

import hello.dto.user.CustomOAuth2User;
import hello.dto.user.EditDTO;
import hello.entity.genre.Genre;
import hello.entity.genre.UserGenre;
import hello.entity.user.Address;
import hello.entity.user.Image;
import hello.entity.user.ProfileImage;
import hello.entity.user.User;
import hello.repository.*;
import hello.service.exception.UserNotFoundException;
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
    public void editUserProcess(Long id, EditDTO editDTO, HttpSession session) throws IOException {
        User findUser = findUserById(id);
        findUser.setEmailConsent(editDTO.isEmailConsent());
        findUser.setGender(editDTO.getGender());
        findUser.setNickname(editDTO.getNickname());
        session.setAttribute("nickname", editDTO.getNickname());
        findUser.setPhone(editDTO.getPhone());
        findUser.setHomeAddress(new Address(editDTO.getZipcode(), editDTO.getAddress(), editDTO.getDetailAddress()));

        // 장르 설정
        userGenreRepository.deleteByUser(id);
        List<String> genres = editDTO.getSelectedGenres();
        for (String genre : genres) {
            System.out.println("genre = " + genre);
        }
        List<Genre> allGenres = genreRepository.findByNameIn(genres);

        allGenres.forEach(genre -> {
            UserGenre userGenre = new UserGenre(findUser, genre);
            findUser.getUserGenres().add(userGenre);
        });

        MultipartFile uploadImage = editDTO.getProfileImage();
        ProfileImage currentImage = findUser.getProfileImage();

        // 사용자가 사진을 업로드한 경우
        if (uploadImage != null && !uploadImage.isEmpty()) {
            // 이미 기존 사진이 있다면 삭제
            if (currentImage != null) {
                clearProfileImage(id, findUser, currentImage);
            }

            Image image = fileStore.storeFile(uploadImage); // 서버에 이미지 저장

            ProfileImage newProfileImage = new ProfileImage(image.getStoreImageName(), image.getImagePath(), findUser);
            findUser.setProfileImage(newProfileImage);

            session.setAttribute("profileImageName", newProfileImage.getStoreImageName());
        }

        // 기본 이미지 사용 버튼을 클릭한 경우
        boolean useDefaultImage = editDTO.isUseDefaultImage();
        if (useDefaultImage && currentImage != null) {
            clearProfileImage(id, findUser, currentImage);
            session.setAttribute("profileImageName", null);
        }
    }

    private void clearProfileImage(Long id, User findUser, ProfileImage currentImage) {
        findUser.setProfileImage(null);
        profileImageRepository.deleteByUserId(id);
        fileStore.deleteFile(currentImage.getStoreImageName());
    }

    public User validateUser(CustomOAuth2User user, Long id) {
        User loginUser = getLoginUserDetail(user);
        if (loginUser != null && loginUser.getId().equals(id)) {
            return loginUser;
        }
        return null;
    }
}