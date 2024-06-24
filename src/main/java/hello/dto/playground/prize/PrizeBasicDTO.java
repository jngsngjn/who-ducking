package hello.dto.playground.prize;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrizeBasicDTO {

    private Long id;
    private String name;
    private String imageName;
    private LocalDate endDate;

    public PrizeBasicDTO(Long id, String name, String imageName, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.endDate = endDate;
    }
}