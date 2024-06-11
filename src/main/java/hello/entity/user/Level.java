package hello.entity.user;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "max_exp")
    private int maxExp;

    @Column(name = "image_name")
    private String imageName;

    public Level() {
    }

    public Level(int maxExp, String imageName) {
        this.maxExp = maxExp;
        this.imageName = imageName;
    }
}