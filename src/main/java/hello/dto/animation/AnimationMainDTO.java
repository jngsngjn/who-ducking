package hello.dto.animation;

import lombok.Data;

@Data
public class AnimationMainDTO {

    private Long id;
    private String imageName;
    private double score;

    public AnimationMainDTO(Long id, String imageName) {
        this.id = id;
        this.imageName = imageName;
    }
}