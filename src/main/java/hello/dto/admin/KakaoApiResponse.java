package hello.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class KakaoApiResponse {

    private List<Document> documents;

    @Data
    public static class Document {
        private String address_name;
        private String latitude; // 위도
        private String longitude; // 경도
    }
}