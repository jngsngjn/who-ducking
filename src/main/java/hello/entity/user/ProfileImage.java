package hello.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profile_image")
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class ProfileImage extends Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public ProfileImage(String storeImageName, String imagePath, User user) {
        super(storeImageName, imagePath);
        this.user = user;
    }

    public ProfileImage() {
    }

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}