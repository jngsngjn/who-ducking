package hello.repository.db;

import hello.entity.board.Board;
import hello.entity.board.Bookmark;
import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    void deleteByUserIdAndBoardId(Long userId, Long boardId);
    boolean existsByUserAndBoard(User user, Board board);
}
