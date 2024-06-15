package hello.dto.animation;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ReviewDTO {

    private Long id;
    private Long userId;
    private Long animationId;
    private String content;
    private Double score;
    private LocalDateTime writeDate;
    private int likeCount;
    private int dislikeCount;
}