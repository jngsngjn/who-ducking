package hello.dto.user;

import hello.entity.user.Gender;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UserInfoEditDTO {

    private MultipartFile profileImage;
    private boolean emailConsent;
    private Gender gender;
    private boolean useDefaultImage;
    private String nickname;
    private String phone;
    private String zipcode;
    private String address;
    private String detailAddress;
    private List<String> selectedGenres;
}