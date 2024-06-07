package hello.security.exception;

import hello.dto.oauth2.OAuth2Response;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AdditionalInfoRequiredException extends AuthenticationException {

    private final OAuth2Response oAuth2Response;

    public AdditionalInfoRequiredException(OAuth2Response oAuth2Response) {
        super("Additional information is required");
        this.oAuth2Response = oAuth2Response;
    }
}