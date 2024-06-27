package hello.dto.playground;

import lombok.Data;

@Data
public class WorldCupDTO {

    private Long id;
    private String name;
    private String imageName;

    public WorldCupDTO(Long id, String name, String imageName) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
    }
}