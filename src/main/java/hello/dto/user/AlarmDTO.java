package hello.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class AlarmDTO {

    private int alarmCount;
    private List<AlarmResponse> alarms;

    @Data
    public static class AlarmResponse {
        private Long id;
        private String message;
        private String link;

        public AlarmResponse() {
        }

        public AlarmResponse(Long id, String message, String link) {
            this.id = id;
            this.message = message;
            this.link = link;
        }
    }
}