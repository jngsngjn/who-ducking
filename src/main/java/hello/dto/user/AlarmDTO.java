package hello.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class AlarmDTO {

    private int alarmCount;
    private List<AlarmResponse> alarms;

    @Data
    public static class AlarmResponse {
        private String message;
        private String link;

        public AlarmResponse() {
        }

        public AlarmResponse(String message, String link) {
            this.message = message;
            this.link = link;
        }
    }
}