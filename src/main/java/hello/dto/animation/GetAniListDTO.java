package hello.dto.animation;

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
    private Long genreId1;
    private Long genreId2;

    public GetAniListDTO(long animationId, String imageName, Double score, long reviewCount, String name) {
        this.animationId = animationId;
        this.imageName = imageName;
        this.score = score;
        this.reviewCount = reviewCount;
        this.name = name;
    }

    public GetAniListDTO() {
    }

    public GetAniListDTO(Long animationId, String imageName, double score, long reviewCount, String name, Long genreId1, Long genreId2) {
        this.animationId = animationId;
        this.imageName = imageName;
        this.score = score;
        this.reviewCount = reviewCount;
        this.name = name;
        this.genreId1 = genreId1;
        this.genreId2 = genreId2;
    }

    public String getReviewCount() {
        return String.valueOf(reviewCount);
    }
}
