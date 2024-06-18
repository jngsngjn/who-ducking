package hello.dto.admin;

import hello.entity.animation.AnimationRating;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class AnimationDTO {

    private String name;
    private String author;
    private LocalDate firstDate;
    private boolean isFinished;
    private AnimationRating rating;
    private String description;
    private MultipartFile image;
    private List<String> genres;
}