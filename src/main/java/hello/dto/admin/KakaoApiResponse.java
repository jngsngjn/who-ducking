package hello.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class KakaoApiResponse {

    private List<Document> documents;

    @Data
    public static class Document {
        private String address_name; // 주소명
        private String y; // 위도 (latitude)
        private String x; // 경도 (longitude)
    }
}