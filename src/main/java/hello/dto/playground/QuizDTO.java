package hello.dto.playground;

import lombok.Data;

@Data
public class QuizDTO {
    private Long id;
    private String question;
    private String answer;
    private String imageName;

    public QuizDTO(Long id, String question, String answer, String imageName) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.imageName = imageName;
    }
}
