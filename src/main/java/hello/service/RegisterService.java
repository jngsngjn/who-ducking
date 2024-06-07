package hello.service;

import hello.dto.oauth2.OAuth2Response;
import hello.dto.user.RegisterDTO;
import hello.entity.user.User;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private final PointService pointService;

    public void registerProcess(OAuth2Response oauth2Response, RegisterDTO registerDTO) {
        User user = new User();

        user.setSocialType(oauth2Response.getProvider());
        user.setUsername(oauth2Response.getProvider() + " " + oauth2Response.getProviderId());
        user.setNickname(registerDTO.getNickname());
        user.setGender(registerDTO.getGender());
        smsService.sendCode(registerDTO.getPhone());
        user.setPhone(passwordEncoder.encode(registerDTO.getPhone()));
        user.setEmail(oauth2Response.getEmail());
        user.setEmailConsent(registerDTO.isEmailConsent());
        user.setRole("ROLE_USER");

        String recommender = registerDTO.getRecommender();
        recommenderProcess(user, recommender);
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
}