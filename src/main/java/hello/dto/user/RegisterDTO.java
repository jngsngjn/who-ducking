package hello.dto.user;

import hello.entity.user.Gender;
import lombok.Data;

@Data
public class RegisterDTO {

    private boolean emailConsent;
    private String nickname;
    private String phone;
    private Gender gender;
}