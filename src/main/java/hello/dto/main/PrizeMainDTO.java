package hello.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrizeMainDTO {

    private Long prizeId;
    private String prizeName;
    private String imageName;
}