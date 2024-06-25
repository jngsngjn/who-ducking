package hello.entity.review;

import hello.entity.animation.Animation;
import hello.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity @Getter @Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "animation_id")
    private Animation animation;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private Double score;

    @CreationTimestamp
    @Column(name = "write_date")
    private LocalDateTime writeDate;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "dislike_count")
    private int dislikeCount;

    @Column(name = "is_spoiler", nullable = false)
    private boolean isSpoiler;
}