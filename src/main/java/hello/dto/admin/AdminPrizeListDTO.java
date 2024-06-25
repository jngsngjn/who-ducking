package hello.dto.admin;

import hello.entity.prize.PrizeGrade;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminPrizeListDTO {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private PrizeGrade grade;
    private String nickname;

    public AdminPrizeListDTO(Long id, String name, LocalDate startDate, LocalDate endDate, PrizeGrade grade) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
    }

    public AdminPrizeListDTO(Long id, String name, LocalDate startDate, LocalDate endDate, PrizeGrade grade, String nickname) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
        this.nickname = nickname;
    }
}