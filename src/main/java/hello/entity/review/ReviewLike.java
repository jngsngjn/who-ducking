package hello.entity.review;

import hello.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name="Review_like")
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="review_id")
    private Review reviewId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;

    @Column(name="is_like")
    private Boolean isLike;

    public void toggleLike() {
        if (isLike == null) {
            isLike = true;
        } else if (isLike) {
            isLike = null;
        } else {
            isLike = true;
        }
    }

}
