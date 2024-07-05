package hello.dto.admin;

import hello.entity.user.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoDTO {

    private String socialType;
    private String nickname;
    private Gender gender;
    private String email;
    private Long level;
    private LocalDate joinDate;

    public UserInfoDTO(String socialType, String nickname, Gender gender, String email, Long level, LocalDate joinDate) {
        this.socialType = socialType;
        this.nickname = nickname;
        this.gender = gender;
        this.email = email;
        this.level = level;
        this.joinDate = joinDate;
    }
}