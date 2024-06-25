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
    private String name;
    private Double score;
    private long reviewCount;

    public GetAniListDTO(long animationId, String imageName, Double score, long reviewCount, String name) {
        this.animationId = animationId;
        this.imageName = imageName;
        this.score = score;
        this.reviewCount = reviewCount;
        this.name = name;
    }

    public String getReviewCount() {
        return String.valueOf(reviewCount);
    }
}
