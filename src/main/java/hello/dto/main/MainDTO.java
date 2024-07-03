package hello.dto.main;

import lombok.Data;

@Data
public class MainDTO {
    private Long id;
    private String title;
    private String imageName;

    public MainDTO(Long id, String title, String imageName) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
    }
}
