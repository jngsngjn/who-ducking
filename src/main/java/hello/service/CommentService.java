package hello.service;

import hello.entity.board.Comment;
import hello.repository.CommentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public void createComment(Comment comment) {
        commentRepository.save(comment);
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

