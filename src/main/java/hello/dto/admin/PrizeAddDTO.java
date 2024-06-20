package hello.dto.admin;

import hello.entity.prize.PrizeGrade;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class PrizeAddDTO {

    private String name;
    private PrizeGrade grade;
    private LocalDateTime endDateTime;
    private MultipartFile image;
}