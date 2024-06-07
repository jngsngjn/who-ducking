package hello.entity.genre;

import hello.entity.animation.Animation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "animation_genre")
@Getter @Setter
public class AnimationGenre {

    @EmbeddedId
    private AniGenreId id;

    @ManyToOne
    @MapsId("animationId")
    @JoinColumn(name = "animation_id")
    private Animation animation;

    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public AnimationGenre() {
    }

    public AnimationGenre(Animation animation, Genre genre) {
        this.id = new AniGenreId(animation.getId(), genre.getId());
        this.animation = animation;
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationGenre that = (AnimationGenre) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}