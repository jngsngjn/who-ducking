package hello.dto.animation;

import lombok.Data;

@Data
public class GetAniListDTO {

    private long id;

    private String image_path;

    private int like_count;

    private double score;

}
