package hello.service;

import hello.dto.user.CustomOAuth2User;
import hello.dto.user.RegisterDTO;
import hello.entity.user.User;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;

    public void registerProcess(CustomOAuth2User oAuth2User, RegisterDTO registerDTO) {
        User user = userRepository.findByUsername(oAuth2User.getUsername());

        user.setNickname(registerDTO.getNickname());
        user.setPhone(registerDTO.getPhone());
        user.setGender(registerDTO.getGender());
        user.setEmailConsent(registerDTO.isEmailConsent());
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
}