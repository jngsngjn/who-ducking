package hello.dto.user;

import hello.entity.user.Gender;
import lombok.Data;

@Data
public class RegisterBasicDTO {

    private boolean emailConsent;
    private String nickname;
    private String phone;
    private Gender gender;
    private String recommender;
}