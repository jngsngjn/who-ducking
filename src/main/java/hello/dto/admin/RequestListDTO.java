package hello.dto.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestListDTO {

    private Long id;
    private String title;
    private String nickname;
    private LocalDate writeDate;

    public RequestListDTO(Long id, String title, String nickname, LocalDate writeDate) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.writeDate = writeDate;
    }
}