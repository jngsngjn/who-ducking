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

    // 리뷰 get용 필드 추가하기
    private Long reviewId;
    private int likeCount;
    private int dislikeCount;
    private LocalDateTime writeDate;
    private double score;
    private String reviewContent;

    // userid로 리뷰작성자도 찾아와야함
    private Long userId;
    private String userNickname;



//    public GetAniDetailDTO(Long animationId, String name, String author, String description, LocalDate firstDate,
//                           String imagePath, Boolean isFinished, AnimationRating rating) {
//    }

    public GetAniDetailDTO(Long animationId, String name, String author, String description, LocalDate firstDate,
                           String imagePath, Boolean isFinished, AnimationRating rating, Long reviewId, int likeCount,
                           int dislikeCount, LocalDateTime writeDate, double score, String reviewContent, Long userId, String userNickname) {
        this.animationId = animationId;
        this.name = name;
        this.author = author;
        this.description = description;
        this.firstDate = firstDate;
        this.imagePath = imagePath;
        this.isFinished = isFinished;
        this.rating = rating;
        this.reviewId = reviewId;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.writeDate = writeDate;
        this.score = score;
        this.reviewContent = reviewContent;
        this.userId = userId;
        this.userNickname = userNickname;
    }
}



// 리뷰 작성하려고 임시 작성
//package hello.dto.animation;
//
//import hello.entity.animation.AnimationRating;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//public class GetAniDetailDTO {
//
//    private Long animationId;
//    private String name;
//    private String author;
//    private String description;
//    private LocalDate firstDate;
//    private String imagePath;
//    private Boolean isFinished;
//    private AnimationRating rating;
//
//    public GetAniDetailDTO(Long animationId, String name, String author, String description, LocalDate firstDate,
//                           String imagePath, Boolean isFinished, AnimationRating rating) {
//        this.animationId = animationId;
//        this.name = name;
//        this.author = author;
//        this.description = description;
//        this.firstDate = firstDate;
//        this.imagePath = imagePath;
//        this.isFinished = isFinished;
//        this.rating = rating;
//    }
//}
