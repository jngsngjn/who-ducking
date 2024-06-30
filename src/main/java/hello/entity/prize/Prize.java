package hello.entity.prize;

import hello.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
public class Prize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_path")
    private String imagePath;

    @Enumerated(EnumType.STRING)
    private PrizeGrade grade;

    @Column(name = "start_date")
    @CreationTimestamp
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "prize")
    private List<Entry> entries = new ArrayList<>();

    public Prize(String name, String imageName, PrizeGrade grade, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.imageName = imageName;
        this.grade = grade;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Prize() {
    }
}