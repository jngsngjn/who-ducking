package hello.entity.genre;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AniGenreId implements Serializable {

    @Column(name = "animation_id")
    private Long animationId;

    @Column(name = "genre_id")
    private Long genreId;

    public AniGenreId() {
    }

    public AniGenreId(Long animationId, Long genreId) {
        this.animationId = animationId;
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AniGenreId that = (AniGenreId) o;
        return Objects.equals(animationId, that.animationId) && Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animationId, genreId);
    }
}