package hello.config;

import hello.security.CustomClientRegistration;
import hello.security.handler.CustomAuthenticationFailureHandler;
import hello.service.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomClientRegistration clientRegistration;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/oauth2/**", "/login", "/register/**").permitAll()
                .requestMatchers("/announcement", "/faq").permitAll()
                .requestMatchers("/popup", "/api/popup-stores").permitAll()
                .requestMatchers("/playground").permitAll()
                .requestMatchers("/animations", "/animations/*").permitAll()
                .requestMatchers("/css/**", "/jpg/**", "/png/**", "/js/**", "/images/**", "/image/**", "/vendor/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        http.csrf(csrf -> csrf.disable());
        http.formLogin(login -> login.disable());
        http.httpBasic(basic -> basic.disable());

        http.oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .clientRegistrationRepository(clientRegistration.clientRegistrationRepository())
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .failureHandler(customAuthenticationFailureHandler))
                .logout(logout -> logout.logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSEIONID"));

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(accessDeniedHandler()));

        return http.build();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            request.getRequestDispatcher("/error/403").forward(request, response);
        };
    }
}