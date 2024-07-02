package hello.dto.search;

import lombok.Data;

@Data
public class SearchAnimationDTO {
    private Long id;
    private String title;
    private String imageName;
    private int reviewCount;
    private double score;

    public SearchAnimationDTO(Long id, String title, String imageName) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
    }
}