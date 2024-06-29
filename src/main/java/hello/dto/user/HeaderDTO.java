package hello.dto.user;

import lombok.Data;

@Data
public class HeaderDTO {

    private int point;
    private int currentExp;
    private int maxExp;
    private long level;

    public HeaderDTO(int point, int currentExp, int maxExp, long level) {
        this.point = point;
        this.currentExp = currentExp;
        this.maxExp = maxExp;
        this.level = level;
    }
}