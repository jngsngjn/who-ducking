package hello.dto.user;

import hello.entity.user.Gender;
import lombok.Data;

@Data
public class EditDTO {

    private String nickname;

    private boolean emailConsent;

    private String phone;

    private Gender gender;
//
//    private List<Genre> genres;
//
//    private String address;
}