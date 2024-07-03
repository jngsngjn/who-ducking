package hello.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LastedAnimationsDTO {

    private Long id;
    private String title;
    private String imageName;
}