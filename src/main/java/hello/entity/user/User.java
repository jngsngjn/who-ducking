package hello.entity.user;

import hello.entity.board.Board;
import hello.entity.board.Bookmark;
import hello.entity.board.Comment;
import hello.entity.genre.Genre;
import hello.entity.genre.UserGenre;
import hello.entity.genre.UserGenreId;
import hello.entity.prize.Entry;
import hello.entity.prize.Prize;
import hello.entity.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_type")
    private String socialType;

    private String username;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    private String email;

    @Column(name = "email_consent")
    private Boolean emailConsent;

    @Embedded
    private Address homeAddress;

    @Column(name = "join_date")
    @CreationTimestamp
    private LocalDate joinDate;

    private int point;

    @ManyToOne
    @JoinColumn(name = "recommender_id")
    private User recommender;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "last_draw_date")
    private LocalDate lastDrawDate;

    private String role;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @Column(name = "current_exp")
    private int currentExp;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGenre> userGenres = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Prize> prizes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    public void addGenre(Genre genre) {
        UserGenre userGenre = new UserGenre(this, genre);
        this.userGenres.add(userGenre);
        genre.getUserGenres().add(userGenre);
    }

    public void removeGenre(Genre genre) {
        UserGenreId userGenreId = new UserGenreId(this.getId(), genre.getId());
        userGenres.removeIf(userGenre -> userGenre.getId().equals(userGenreId));
        genre.getUserGenres().removeIf(userGenre -> userGenre.getId().equals(userGenreId));
    }
}