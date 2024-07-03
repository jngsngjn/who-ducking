package hello.dto.animation;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewLikeDTO {
    private Long reviewLikeId;
    private Long reviewId;
    private Long userId;
    private Boolean isLike;
    private Boolean isDislike;

    public ReviewLikeDTO(Long reviewLikeId,Long reviewId, Long userId, Boolean isLike, Boolean isDislike) {
       this.reviewLikeId = reviewLikeId;
        this.reviewId = reviewId;
        this.userId = userId;
        this.isLike = isLike;
        this.isDislike = isDislike;
    }
}
