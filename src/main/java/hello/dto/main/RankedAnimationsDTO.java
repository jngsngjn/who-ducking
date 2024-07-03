package hello.dto.main;

import lombok.Data;

@Data
public class RankedAnimationsDTO {

    private Long id;
    private String imageName;
    private double score;

    public RankedAnimationsDTO(Long id, String imageName) {
        this.id = id;
        this.imageName = imageName;
    }
}