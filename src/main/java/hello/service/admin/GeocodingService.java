package hello.service.admin;

import hello.dto.admin.KakaoApiResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

    @Value("${kakao.client-id}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public GeoLocation getCoordinates(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoApiResponse.class);
        KakaoApiResponse apiResponse = response.getBody();

        if (apiResponse != null && !apiResponse.getDocuments().isEmpty()) {
            KakaoApiResponse.Document document = apiResponse.getDocuments().get(0);
            String latitude = document.getY();
            String longitude = document.getX();
            if (latitude == null || longitude == null) {
                throw new RuntimeException("위도와 경도 값을 찾을 수 없습니다.");
            }
            return new GeoLocation(Double.parseDouble(document.getY()), Double.parseDouble(document.getX()));
        } else {
            throw new RuntimeException("주소를 변환할 수 없습니다: " + address);
        }
    }

    @Getter
    public static class GeoLocation {
        private final double latitude; // 위도
        private final double longitude; // 경도

        public GeoLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}