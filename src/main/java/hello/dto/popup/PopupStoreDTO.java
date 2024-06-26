package hello.dto.popup;

import hello.entity.popup.PopupStore;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class PopupStoreDTO {

    private Long id;
    private String name;
    private String openTime;
    private String closeTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private double latitude;
    private double longitude;
    private String imagePath;
    private String imageName;
    private boolean bookmarked;
    private String address; // 주소 필드 추가

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public PopupStoreDTO(PopupStore popupStore, boolean bookmarked) {
        this.id = popupStore.getId();
        this.name = popupStore.getName();
        this.openTime = popupStore.getOpenTime() != null ? popupStore.getOpenTime().format(TIME_FORMATTER) : ""; // 문자열로 변환
        this.closeTime = popupStore.getCloseTime() != null ? popupStore.getCloseTime().format(TIME_FORMATTER) : ""; // 문자열로 변환
        this.startDate = popupStore.getStartDate();
        this.endDate = popupStore.getEndDate();
        this.latitude = popupStore.getLatitude();
        this.longitude = popupStore.getLongitude();
        this.imagePath = popupStore.getImagePath();
        this.imageName = popupStore.getImageName();
        this.bookmarked = bookmarked;
        this.address = popupStore.getAddress().getAddress() + " " + popupStore.getAddress().getDetailedAddress(); // 주소 설정
    }
}