package hello.repository.db;

import hello.entity.board.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :boardId")
    @Transactional
    void incrementViewCount(@Param("boardId") Long boardId);

    //조회수 순
    Page<Board> findAllByOrderByViewCountDesc(Pageable pageable);

    //최신순
    Page<Board> findAllByOrderByWriteDateDesc(Pageable pageable);

    //메인화면 게시판 미리보기
    List<Board> findAllByOrderByViewCountAsc();

    //신고 횟수 증가
    @Modifying
    @Query("UPDATE Board b SET b.reportCount = b.reportCount + 1 WHERE b.id = :boardId")
    @Transactional
    void incrementReportCount(@Param("boardId") Long boardId);

}
