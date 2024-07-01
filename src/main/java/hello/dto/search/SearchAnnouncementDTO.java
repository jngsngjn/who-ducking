package hello.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchAnnouncementDTO {

    private Long id;
    private String title;
    private String content;
}