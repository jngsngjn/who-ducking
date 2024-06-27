package hello.dto.admin;

import lombok.Data;

@Data
public class PrizeDrawDTO {

    private Long id;
    private String name;
    private String imageName;
    private long entryCount;

    public PrizeDrawDTO(Long id, String name, String imageName, long entryCount) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.entryCount = entryCount;
    }
}