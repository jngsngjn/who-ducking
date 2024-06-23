package hello.dto.notice;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NoticeDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDate writeDate;

    public NoticeDTO(Long id, String title, String content, LocalDate writeDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
    }
}