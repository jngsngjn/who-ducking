package hello.entity.animation;

import hello.entity.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
public class Animation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private AnimationRating rating;

    @Column(name = "first_date")
    private LocalDate firstDate;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_path")
    private String imagePath;

    private Boolean isFinished;

    @Column(insertable = false)
    private Boolean existReview;

    @OneToMany(mappedBy = "animation")
    private List<Review> reviews = new ArrayList<>();
}