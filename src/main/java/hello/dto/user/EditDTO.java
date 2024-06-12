package hello.dto.user;

import hello.entity.user.Gender;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EditDTO {


    private MultipartFile profileImage;
    private boolean emailConsent;
    private Gender gender;

//    private String nickname;
//    private String phone;



//
//    private List<Genre> genres;
//
//    private String address;
}