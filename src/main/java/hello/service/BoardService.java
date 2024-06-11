package hello.service;

import hello.entity.board.Board;
import hello.repository.BoardRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //게시판 목록
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    //글작성
    public void createBoard(Board board) {
        boardRepository.save(board);
    }

    //글 수정
    public Board updateBoard(Long boardId, Board updateboard) throws NotFoundException {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            board.setTitle(updateboard.getTitle());
            board.setContent(updateboard.getContent());
            return boardRepository.save(board);
        }
        else{
            throw new NotFoundException("Board not found");
        }
    }

    //글 삭제
    public void deleteBoard(Long boardId) throws NotFoundException {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            boardRepository.deleteById(boardId);
        }
        else{
            throw new NotFoundException("게시물을 찾을 수 없습니다.");
        }
    }

    //게시판 아이디 가져오기
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시판 :" + boardId));
    }
}
