package hello.service;

import hello.dto.oauth2.OAuth2Response;
import hello.dto.user.RegisterBasicDTO;
import hello.entity.genre.Genre;
import hello.entity.genre.UserGenre;
import hello.entity.user.Address;
import hello.entity.user.User;
import hello.repository.GenreRepository;
import hello.repository.LevelRepository;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final LevelRepository levelRepository;

    private final SmsService smsService;
    private final PointService pointService;

    public void registerProcess(OAuth2Response oauth2Response, RegisterBasicDTO registerBasicDTO, List<String> genres) {
        User user = new User();

        // 일반 정보 저장
        user.setSocialType(oauth2Response.getProvider());
        user.setUsername(oauth2Response.getProvider() + " " + oauth2Response.getProviderId());
        user.setNickname(registerBasicDTO.getNickname());
        user.setGender(registerBasicDTO.getGender());
        smsService.sendCode(registerBasicDTO.getPhone());
        user.setPhone(registerBasicDTO.getPhone());
        user.setEmail(oauth2Response.getEmail());
        user.setEmailConsent(registerBasicDTO.isEmailConsent());
        user.setRole("ROLE_USER");
        user.setLevel(levelRepository.findById(1L).get());
        user.setHomeAddress(new Address("", "", ""));

        // 추천인 로직
        String recommender = registerBasicDTO.getRecommender();
        recommenderProcess(user, recommender);

        // 선택한 장르 저장
        List<Genre> allGenres = genreRepository.findByNameIn(genres);
        allGenres.forEach(genre -> {
            UserGenre userGenre = new UserGenre(user, genre);
            user.getUserGenres().add(userGenre);
        });

        userRepository.save(user);
    }

    public boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public void recommenderProcess(User user, String recommender) {
        User findUser = userRepository.findByNickname(recommender);
        if (findUser != null) {
            user.setRecommender(findUser);
            pointService.increasePoint(user, 20);
            pointService.increasePoint(findUser, 20);
        }
    }

    public Map<String, Object> isDuplicatePhone(String inputPhone) {
        Map<String, Object> result = new HashMap<>();
        List<String> phones = userRepository.findAllPhoneNumbers();

        for (String phone : phones) {
            if (phone.equals(inputPhone)) {
                String socialType = userRepository.findSocialTypeByPhone(phone);
                result.put("isDuplicate", true);
                result.put("socialType", socialType);
                return result;
            }
        }
        result.put("isDuplicate", false);
        return result;
    }

    public boolean recommenderCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}