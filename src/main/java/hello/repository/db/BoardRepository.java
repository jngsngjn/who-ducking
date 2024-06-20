package hello.repository.db;

import hello.entity.board.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :boardId")
    @Transactional
    void incrementViewCount(@Param("boardId") Long boardId);

    //조회수 순
    Page<Board> findAllByOrderByViewCountDesc(Pageable pageable);

    //최신순
    Page<Board> findAllByOrderByWriteDateDesc(Pageable pageable);
}
