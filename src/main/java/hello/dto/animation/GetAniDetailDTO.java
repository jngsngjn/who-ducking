package hello.dto.animation;

import hello.entity.animation.AnimationRating;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetAniDetailDTO {

    private Long animationId;

    private String name;

    private String author;

    private String description;

    private LocalDate firstDate;

    private String imagePath;

    private Boolean isFinished;

    private AnimationRating rating;

    public GetAniDetailDTO(Long animationId, String name, String author, String description, LocalDate firstDate,
                           String imagePath, Boolean isFinished, AnimationRating rating) {
        this.animationId = animationId;
        this.name = name;
        this.author = author;
        this.description = description;
        this.firstDate = firstDate;
        this.imagePath = imagePath;
        this.isFinished = isFinished;
        this.rating = rating;
    }


    //애니 id로 조회해서 가지고 올때 작성된 리뷰들을 가져와야하는데  리뷰 id들을 또 뭉쳐서 데이터를 가져와야하나?
    // 1. reviewDTO를 생성해야할거같은데
    //    private List<ReviewDTO> reviews;
    // 2. 한번에 Review 정보들까지 받아오면 어떻게 될까? 안될거같은데
//    private Long reviewId;
//
//    private Long likeCount;
//
//    private Long dislikeCount;
//
//    private LocalDateTime writeDate;
//
//    private double score;
//
//    private String content;
}

