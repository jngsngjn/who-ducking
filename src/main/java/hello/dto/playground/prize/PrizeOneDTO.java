package hello.dto.playground.prize;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrizeOneDTO {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageName;

    public PrizeOneDTO(Long id, String name, LocalDate startDate, LocalDate endDate, String imageName) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageName = imageName;
    }
}