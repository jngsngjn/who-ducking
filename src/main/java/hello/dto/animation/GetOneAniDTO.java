package hello.dto.animation;

import lombok.Data;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Data
public class GetOneAniDTO {
    private long id;

    private int isFinished;

    private String first_date;

    private String author;

    private String image_path;

    private String name;

    private String description;

    private String rating;

    private double score;

    private int genre_id;
}

