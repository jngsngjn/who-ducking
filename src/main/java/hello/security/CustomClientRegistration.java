package hello.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
@RequiredArgsConstructor
public class CustomClientRegistration {

    private final SocialClientRegistration socialClientRegistration;

    public ClientRegistrationRepository clientRegistrationRepository() {

        return new InMemoryClientRegistrationRepository(
                socialClientRegistration.googleClientRegistration(),
                socialClientRegistration.naverClientRegistration(),
                socialClientRegistration.kakaoClientRegistration());
    }
}