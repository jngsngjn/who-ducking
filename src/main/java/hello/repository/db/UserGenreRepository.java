package hello.repository.db;

import hello.entity.genre.Genre;
import hello.entity.genre.UserGenre;
import hello.entity.genre.UserGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGenreRepository extends JpaRepository<UserGenre, UserGenreId> {

    @Modifying
    @Query("delete from UserGenre ug where ug.user.id = :userId")
    void deleteByUser(@Param("userId") Long userId);

    List<UserGenre> findByGenreIn(List<Genre> genres);
}