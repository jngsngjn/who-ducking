package hello.dto.admin;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PopupStoreAddDTO {

    private String name;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String zipcode;
    private String address;
    private String detailedAddress;
    private MultipartFile image;
}