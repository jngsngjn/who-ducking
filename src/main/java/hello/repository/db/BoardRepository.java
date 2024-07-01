package hello.repository.db;

import hello.dto.board.MyBoardDTO;
import hello.dto.board.MyBookmarkDTO;
import hello.dto.search.SearchBoardDTO;
import hello.entity.board.Board;
import hello.entity.user.User;
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
    List<Board> findTop5ByOrderByWriteDateDesc();

    //신고 횟수 증가
    @Modifying
    @Query("UPDATE Board b SET b.reportCount = b.reportCount + 1 WHERE b.id = :boardId")
    @Transactional
    void incrementReportCount(@Param("boardId") Long boardId);

    @Query("select new hello.dto.board.MyBoardDTO(b.id, b.title, b.writeDate, b.viewCount) from Board b where b.user = :user order by b.writeDate desc")
    Page<MyBoardDTO> findMyBoardsOrderByWriteDate(@Param("user") User user, Pageable pageable);

    @Query("select new hello.dto.board.MyBoardDTO(b.id, b.title, b.writeDate, b.viewCount) from Board b where b.user = :user order by b.viewCount desc")
    Page<MyBoardDTO> findMyBoardsOrderByViewCount(@Param("user") User user, Pageable pageable);

    @Query("select new hello.dto.board.MyBookmarkDTO(b.id, b.title, b.writeDate, b.viewCount) from Board b join Bookmark bm on b.id = bm.board.id where bm.user = :user order by b.writeDate desc")
    Page<MyBookmarkDTO> findBookmark(@Param("user") User user, Pageable pageable);

    @Query("SELECT new hello.dto.search.SearchBoardDTO(b.id, b.title, b.content) FROM Board b WHERE b.title LIKE %:name% OR b.content LIKE %:name%")
    List<SearchBoardDTO> findByTitleOrContentContaining(@Param("name") String name);
}