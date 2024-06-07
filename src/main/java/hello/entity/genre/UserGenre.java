package hello.entity.genre;

import hello.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_genre")
@Getter @Setter
public class UserGenre {

    @EmbeddedId
    private UserGenreId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public UserGenre() {}

    public UserGenre(User user, Genre genre) {
        this.user = user;
        this.genre = genre;
        this.id = new UserGenreId(user.getId(), genre.getId());
    }
}