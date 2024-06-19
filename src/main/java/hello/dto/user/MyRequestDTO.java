package hello.dto.user;

import hello.entity.request.RequestStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MyRequestDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDate writeDate;
    private RequestStatus status;
    private LocalDate responseDate;
    private String response;

    public MyRequestDTO(Long id, String title, String content, LocalDate writeDate, RequestStatus status, LocalDate responseDate, String response) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
        this.status = status;
        this.responseDate = responseDate;
        this.response = response;
    }
}