package hello.repository;

import hello.entity.genre.UserGenre;
import hello.entity.genre.UserGenreId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGenreRepository extends JpaRepository<UserGenre, UserGenreId> {
}