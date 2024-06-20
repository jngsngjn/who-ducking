package hello.dto.admin;

import hello.entity.prize.PrizeGrade;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PrizeListDTO {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDateTime endDateTime;
    private PrizeGrade grade;
    private String nickname;

    public PrizeListDTO(Long id, String name, LocalDate startDate, LocalDateTime endDateTime, PrizeGrade grade) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDateTime = endDateTime;
        this.grade = grade;
    }

    public PrizeListDTO(Long id, String name, LocalDate startDate, LocalDateTime endDateTime, PrizeGrade grade, String nickname) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDateTime = endDateTime;
        this.grade = grade;
        this.nickname = nickname;
    }
}