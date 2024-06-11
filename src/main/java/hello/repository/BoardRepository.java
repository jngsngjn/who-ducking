package hello.repository;

import hello.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> getAllBoards();

    Board getBoardById(Long boardId);

    void createBoard(Board board);

    void updateBoard(Long boardId, Board updatedBoard);

    void deleteBoard(Long boardId);
}
