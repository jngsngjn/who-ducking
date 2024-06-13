package hello.service;

import hello.dto.board.BoardDTO;
import hello.entity.board.Board;
import hello.entity.user.User;
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
    public void createBoard(BoardDTO writeboard, User loginUser)  {
        Board board = new Board();

        board.setTitle(writeboard.getTitle());
        board.setContent(writeboard.getContent());
        board.setImageName(writeboard.getImageName());
        board.setImagePath(writeboard.getImagePath());
        board.setUser(loginUser);

        boardRepository.save(board);
    }

    //게시판 목록
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    //상세보기
    public Optional<Board> getBoardById(Long id) {
        //조회수 업데이트
        boardRepository.incrementViewCount(id);
        return boardRepository.findById(id);
    }

    //글 수정
    public void updateBoard(Long boardId, BoardDTO updateboard, User loginUser) throws NotFoundException {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            board.setTitle(updateboard.getTitle());
            board.setContent(updateboard.getContent());
            board.setImageName(updateboard.getImageName());
            board.setImagePath(updateboard.getImagePath());
            board.setUser(loginUser);


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

    //최신순
    public List<Board> getBoardsSortedByLatest(){
        return boardRepository.findAllByOrderByWriteDate();
    }

    //조회순
    public List<Board> getBoardsSortedByViewCount(){
        return boardRepository.findAllByOrderByViewCountDesc();
    }


}
