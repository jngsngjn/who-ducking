package hello.dto.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestDetailDTO {

    private Long id;
    private String title;
    private String content;
    private String nickname;
    private LocalDate writeDate;

    public RequestDetailDTO(Long id, String title, String content, String nickname, LocalDate writeDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.writeDate = writeDate;
    }
}