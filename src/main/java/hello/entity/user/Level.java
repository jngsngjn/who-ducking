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

    @Column(name = "image_path")
    private String imagePath;

    public Level() {
    }

    public Level(int maxExp, String imagePath) {
        this.maxExp = maxExp;
        this.imagePath = imagePath;
    }
}