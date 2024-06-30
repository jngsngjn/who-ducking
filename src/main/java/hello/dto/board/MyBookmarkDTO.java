package hello.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyBookmarkDTO {

    private Long id;
    private String title;
    private LocalDateTime writeDateTime;
    private int viewCount;
    private int commentCount;

    public MyBookmarkDTO(Long id, String title, LocalDateTime writeDateTime, int viewCount) {
        this.id = id;
        this.title = title;
        this.writeDateTime = writeDateTime;
        this.viewCount = viewCount;
    }
}