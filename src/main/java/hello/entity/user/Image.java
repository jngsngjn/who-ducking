package hello.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass @Getter @Setter
public class Image {

    @Column(name = "store_image_name")
    private String storeImageName; // 서버에 저장되는 이미지 이름

    @Column(name = "image_path")
    private String imagePath; // 서버 이미지 경로

    public Image(String storeImageName, String imagePath) {
        this.storeImageName = storeImageName;
        this.imagePath = imagePath;
    }

    public Image() {
    }
}