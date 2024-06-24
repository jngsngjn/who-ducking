package hello.dto.admin;

import hello.entity.prize.PrizeGrade;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class AdminPrizeAddDTO {

    private String name;
    private PrizeGrade grade;
    private LocalDate endDate;
    private MultipartFile image;
}