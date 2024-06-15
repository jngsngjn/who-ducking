package hello.dto.animation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAniListDTO {
    private Long animationId;
    private String imagePath;
    private Double score;
    private Long reviewCount;

    public GetAniListDTO(Long animationId, String imagePath, Double score, Long reviewCount) {
        this.animationId = animationId;
        this.imagePath = imagePath;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    public String getReviewCount() {
        return String.valueOf(reviewCount);
    }
}
