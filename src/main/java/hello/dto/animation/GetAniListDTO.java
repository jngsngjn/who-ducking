package hello.dto.animation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data
@Getter
@Setter
public class GetAniListDTO {
    private long animationId;
    private String imageName;
    private Double score;
    private long reviewCount;

    public GetAniListDTO(long animationId, String imageName, Double score, long reviewCount) {
        this.animationId = animationId;
        this.imageName = imageName;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    public String getReviewCount() {
        return String.valueOf(reviewCount);
    }
}
