package hello.entity.animation;

import hello.entity.alarm.Alarm;
import hello.entity.genre.AnimationGenre;
import hello.entity.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter @Setter
public class Animation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private AnimationRating rating;

    @Column(name = "first_date")
    private LocalDate firstDate;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_finished")
    private Boolean isFinished;

    @Column(name = "exist_review")
    private Boolean existReview = false;

    @OneToMany(mappedBy = "animation")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "animation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnimationGenre> animationGenres = new HashSet<>();

    @OneToMany(mappedBy = "animation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();
}