package hello.security.handler;

import hello.exception.AdditionalInfoRequiredException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof AdditionalInfoRequiredException) {
            AdditionalInfoRequiredException additionalInfoException = (AdditionalInfoRequiredException) exception;
            request.getSession().setAttribute("oauth2Response", additionalInfoException.getOAuth2Response());
            getRedirectStrategy().sendRedirect(request, response, "/register/basic");
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}