package hello.repository.db;

import hello.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardIdOrderByIdDesc(Long boardId);

    Integer countByBoardId(Long boardId);
}
