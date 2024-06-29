package hello.service.board;

import hello.dto.board.BoardDTO;
import hello.entity.board.Board;
import hello.entity.user.User;
import hello.repository.db.BoardRepository;
import hello.repository.db.UserRepository;
import hello.repository.server.FileStore;
import hello.service.basic.ExpService;
import hello.service.basic.PointService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileStore fileStore;
    private final PointService pointService;
    private final ExpService expService;

    @Value("${boardPath}")
    private String serverBoardImagePath;

    //글작성
    public void createBoard(BoardDTO writeboard, User loginUser, MultipartFile file) throws Exception {
        // 첫 글일 때만!
        boolean hasPosted = loginUser.isHasPosted();
        if (!hasPosted) {
            pointService.increasePoint(loginUser, 5);
            expService.increaseExp(loginUser, 5, null);
            loginUser.setHasPosted(true);
            userRepository.save(loginUser);
        }

        Board board = new Board();

        if (!file.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(serverBoardImagePath + "/" + filename);
            file.transferTo(saveFile);

            writeboard.setImageName(filename);
            writeboard.setImagePath(serverBoardImagePath + "/" + filename);
            board.setImageName(writeboard.getImageName());
            board.setImagePath(writeboard.getImagePath());
        }

        board.setTitle(writeboard.getTitle());
        board.setContent(writeboard.getContent());
        board.setUser(loginUser);
        boardRepository.save(board);
    }

    //상세보기
    public Optional<Board> getBoardById(Long id) {
        return boardRepository.findById(id);
    }

    //조회수 업데이트
    public void updateViewCount(Long id){
        boardRepository.incrementViewCount(id);
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

            // 게시글 이미지 삭제 추가
            Board board = optionalBoard.get();
            String imageName = board.getImageName();
            if (!imageName.isEmpty()) {
                fileStore.deleteBoardImage(imageName);
            }
        }
        else {
            throw new NotFoundException("게시물을 찾을 수 없습니다.");
        }
    }

    //전체 목록 보기
    //최신순
    public Page<Board> getBoardsSortedByLatest(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findAllByOrderByWriteDateDesc(pageable);
    }

    //조회순
    public Page<Board> getBoardsSortedByViewCount(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findAllByOrderByViewCountDesc(pageable);
    }

    //메인 화면 자유게시판 미리보기
    public List<Board> getBoardsSortedByWriteDateToMain(){
        return boardRepository.findTop5ByOrderByWriteDateDesc();
    }

    //신고 횟수 증가
    public void incrementReportCount(Long boardId){
        boardRepository.incrementReportCount(boardId);
    }
}