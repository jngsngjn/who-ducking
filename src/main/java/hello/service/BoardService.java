package hello.service;

import hello.dto.board.BoardDTO;
import hello.entity.board.Board;
import hello.entity.board.Bookmark;
import hello.entity.user.User;
import hello.repository.BoardRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final String serverBoardImagePath = "C:/Users/smkim/server/board";

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //글작성
    public void createBoard(BoardDTO writeboard, User loginUser, MultipartFile file) throws Exception  {
        Board board = new Board();

        UUID uuid = UUID.randomUUID();

        String filename = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(serverBoardImagePath + "/" + filename);

        file.transferTo(saveFile);

        writeboard.setImageName(filename);
        writeboard.setImagePath(serverBoardImagePath + "/" + filename);

        board.setTitle(writeboard.getTitle());
        board.setContent(writeboard.getContent());
        board.setImageName(writeboard.getImageName());
        board.setImagePath(writeboard.getImagePath());
        board.setUser(loginUser);

        boardRepository.save(board);
    }

    //상세보기
    public Optional<Board> getBoardById(Long id) {
        //조회수 업데이트
        boardRepository.incrementViewCount(id);
        return boardRepository.findById(id);
    }

    //글 수정
    public void updateBoard(Long boardId, BoardDTO updateboard, User loginUser, MultipartFile file) throws Exception {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            UUID uuid = UUID.randomUUID();

            String filename = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(serverBoardImagePath + "/" + filename);

            file.transferTo(saveFile);

            updateboard.setImageName(filename);
            updateboard.setImagePath(serverBoardImagePath + "/" + filename);

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

    //전체 목록 보기
    //최신순
    public Page<Board> getBoardsSortedByLatest(Pageable pageable){
        return boardRepository.findAllByOrderByWriteDateDesc(pageable);
    }

    //조회순
    public Page<Board> getBoardsSortedByViewCount(Pageable pageable){
        return boardRepository.findAllByOrderByViewCountDesc(pageable);
    }


}
