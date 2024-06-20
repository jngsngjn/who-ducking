package hello.dto.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AnnouncementListDTO {

    private Long id;
    private String title;
    private LocalDate writeDate;

    public AnnouncementListDTO(Long id, String title, LocalDate writeDate) {
        this.id = id;
        this.title = title;
        this.writeDate = writeDate;
    }
}