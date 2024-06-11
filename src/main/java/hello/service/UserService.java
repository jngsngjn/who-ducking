package hello.service;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getLoginUserDetail(CustomOAuth2User user) {
        User loginUser = userRepository.findByUsername(user.getUsername());
        Hibernate.initialize(loginUser.getUserGenres()); // userGenres 컬렉션을 초기화
        return loginUser;
    }
}