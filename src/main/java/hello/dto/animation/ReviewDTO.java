package hello.dto.animation;

import lombok.Data;


import java.time.LocalDateTime;

// 보류 -> 작성때 쓸 수 도 있지 않을까

@Data
public class ReviewDTO {

    private String username;

    private int likeCount;

    private int dislikeCount;

    private LocalDateTime writeDate;

    private double score;

    private String content;



}