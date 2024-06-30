package hello.dto.playground;

import lombok.Data;

@Data
public class QuizDTO {

    private String answer;
    private String imageName;

    public QuizDTO(String answer, String imageName) {
        this.answer = answer;
        this.imageName = imageName;
    }
}