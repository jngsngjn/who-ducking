package hello.dto.animation;

import lombok.Data;

@Data
public class MyReviewDTO {

    private long animationId;
    private String title;
    private String imageName;
    private double score;
    private int totalComment;

    public MyReviewDTO(long animationId, String title, String imageName, double score) {
        this.animationId = animationId;
        this.title = title;
        this.imageName = imageName;
        this.score = score;
    }
}