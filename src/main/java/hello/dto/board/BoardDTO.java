package hello.dto.board;

import lombok.Data;

@Data
public class BoardDTO {

    private String title;

    private String content;

    private String nickname;

    private String imageName;

    private String imagePath;
}
