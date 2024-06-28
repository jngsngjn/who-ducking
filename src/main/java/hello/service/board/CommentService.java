package hello.service.board;

import hello.entity.board.Board;
import hello.entity.board.Comment;
import hello.repository.db.CommentRepository;
import hello.service.basic.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AlarmService alarmService;

    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
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
}

