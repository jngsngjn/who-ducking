package hello.dto.admin;

import lombok.Data;

@Data
public class PrizeDrawDTO {

    private Long id;
    private String name;

    public PrizeDrawDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}