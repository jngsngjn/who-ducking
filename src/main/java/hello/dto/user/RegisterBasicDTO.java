package hello.dto.user;

import hello.entity.user.Gender;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterBasicDTO {

    private boolean emailConsent;

    @Length(min = 2, max = 12)
    private String nickname;

    private String phone;

    private Gender gender;

    private String recommender;
}