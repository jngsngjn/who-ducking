package hello.dto.animation;

import lombok.Getter;
import lombok.Setter;

//@Data
@Getter
@Setter
public class GetAniListDTO {
    private long animationId;
    private String name;
    private String imagePath;
    private Double score;
    private long reviewCount;

    public GetAniListDTO(long animationId, String imagePath, Double score, long reviewCount, String name ) {
        this.animationId = animationId;
        this.imagePath = imagePath;
        this.score = score;
        this.reviewCount = reviewCount;
        this.name = name;
    }

    public String getReviewCount() {
        return String.valueOf(reviewCount);
    }
}
