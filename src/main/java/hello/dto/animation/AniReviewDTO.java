package hello.dto.animation;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AniReviewDTO {
    private Long animationId;
    private Long reviewId;
    private String content;
    private Long likeCount;
    private Long dislikeCount;
    private LocalDateTime writeDate;
    private Double score;
    private Long userId;
    private String nickname;
    private Long reviewCount;
    private Double averageScore;
    private Boolean isSpoiler;

    public AniReviewDTO() {}

// 리뷰 작성 dto
    public AniReviewDTO(Long animationId, Long userId, String nickname, String content, Double score, Boolean isSpoiler){
        this.animationId = animationId;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.score = score;
        this.isSpoiler = isSpoiler;
    }

}
