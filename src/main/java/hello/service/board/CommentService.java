package hello.service.board;

import hello.entity.board.Board;
import hello.entity.board.Comment;
import hello.repository.db.CommentRepository;
import hello.service.basic.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AlarmService alarmService;

    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardIdOrderByIdDesc(boardId);
    }

    public void createComment(Comment comment, Board board) {
        commentRepository.save(comment);
        alarmService.commentAlarmService(board);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        return commentRepository.findById(id).map(comment -> {
            comment.setContent(updatedComment.getContent());
            return commentRepository.save(comment);
        }).orElseGet(() -> {
            updatedComment.setId(id);
            return commentRepository.save(updatedComment);
        });
    }

    public int countCommentsByBoardId(Long boardId) {
        return commentRepository.countByBoardId(boardId);
    }
}

