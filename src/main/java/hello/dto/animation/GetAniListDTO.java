package hello.dto.animation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data
@Getter
@Setter
public class GetAniListDTO {
    private long animationId;
    private String imagePath;
    private Double score;
    private long reviewCount;

    public GetAniListDTO(long animationId, String imagePath, Double score, long reviewCount) {
        this.animationId = animationId;
        this.imagePath = imagePath;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    public String getReviewCount() {
        return String.valueOf(reviewCount);
    }
}
