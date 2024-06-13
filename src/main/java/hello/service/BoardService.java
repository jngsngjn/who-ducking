package hello.service;

import hello.dto.board.BoardDTO;
import hello.entity.board.Board;
import hello.repository.BoardRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //글작성
    public void createBoard(BoardDTO writeboard)  {
        Board board = new Board();

        board.setTitle(writeboard.getTitle());
        board.setContent(writeboard.getContent());
        board.setImageName(writeboard.getImageName());
        board.setImagePath(writeboard.getImagePath());

        boardRepository.save(board);
    }

    //게시판 목록
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    //상세보기
    public Optional<Board> getBoardById(Long id) {
        return boardRepository.findById(id);
    }

    //글 수정
    public void updateBoard(Long boardId, BoardDTO updateboard) throws NotFoundException {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            board.setTitle(updateboard.getTitle());
            board.setContent(updateboard.getContent());
            board.setImageName(updateboard.getImageName());
            board.setImagePath(updateboard.getImagePath());


            boardRepository.save(board);
        }
        else{
            throw new NotFoundException("해당 게시물이 존재하지 않습니다.");
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

}
