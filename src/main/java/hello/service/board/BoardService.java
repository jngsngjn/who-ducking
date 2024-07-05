package hello.service.board;

import hello.dto.board.BoardDTO;
import hello.dto.board.MyBoardDTO;
import hello.dto.board.MyBookmarkDTO;
import hello.entity.board.Board;
import hello.entity.user.User;
import hello.repository.db.BoardRepository;
import hello.repository.db.CommentRepository;
import hello.repository.db.UserRepository;
import hello.repository.server.FileStore;
import hello.service.basic.ExpService;
import hello.service.basic.PointService;
import jakarta.servlet.http.HttpSession;
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
    private final CommentRepository commentRepository;

    @Value("${boardPath}")
    private String serverBoardImagePath;

    // 글 작성
    public void createBoard(BoardDTO writeBoard, User loginUser, MultipartFile file, HttpSession session) throws Exception {
        // 첫 글일 때만!
        boolean hasPosted = loginUser.isHasPosted();
        if (!hasPosted) {
            pointService.increasePoint(loginUser, 3); // 시연을 위해 10 포인트에서 3 포인트로 변경
            expService.increaseExp(loginUser, 10, session);
            loginUser.setHasPosted(true);
            userRepository.save(loginUser);
        }

        Board board = new Board();

        if (!file.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(serverBoardImagePath + "/" + filename);
            file.transferTo(saveFile);

            writeBoard.setImageName(filename);
            writeBoard.setImagePath(serverBoardImagePath + "/" + filename);
            board.setImageName(writeBoard.getImageName());
            board.setImagePath(writeBoard.getImagePath());
        }

        board.setTitle(writeBoard.getTitle());
        board.setContent(writeBoard.getContent());
        board.setUser(loginUser);
        boardRepository.save(board);
    }

    //상세보기
    public Optional<Board> getBoardById(Long id) {
        return boardRepository.findById(id);
    }

    //조회수 업데이트
    @Transactional
    public void updateViewCount(Long id){
        boardRepository.incrementViewCount(id);
        System.out.println("조회수 업데이트");
    }

    //글 수정
    public void updateBoardWithNewImage(Long boardId, BoardDTO updateboard, User loginUser, MultipartFile file) throws Exception {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            UUID uuid = UUID.randomUUID();

            if (!file.isEmpty()) {
                String filename = uuid + "_" + file.getOriginalFilename();

                File saveFile = new File(serverBoardImagePath + "/" + filename);

                file.transferTo(saveFile);

                updateboard.setImageName(filename);
                updateboard.setImagePath(serverBoardImagePath + "/" + filename);

                // 사진 수정 시 기존 사진 삭제
                String imageName = board.getImageName();
                fileStore.deleteBoardImage(imageName);
            }

            board.setTitle(updateboard.getTitle());
            board.setContent(updateboard.getContent());
            board.setUser(loginUser);

            if (file != null && !file.isEmpty()) {
                board.setImageName(updateboard.getImageName());
                board.setImagePath(updateboard.getImagePath());
            }
            else {
                // 글 수정 + 사진 삭제 시 서버에서도 사진 삭제
                String imageName = board.getImageName();
                fileStore.deleteBoardImage(imageName);
                board.setImageName(null);
                board.setImagePath(null);
            }

            boardRepository.save(board);
        }
        else{
            throw new NotFoundException("해당 게시물이 존재하지 않습니다.");
        }
    }

    //기존 이미지를 유지하면서 게시글 업데이트
    public void updateBoardWithoutChangingImage(Long boardId, BoardDTO updatedBoard, User loginUser) throws Exception {
        Board board = boardRepository.findById(boardId).orElse(null);

        if (board != null) {

            board.setTitle(updatedBoard.getTitle());
            board.setContent(updatedBoard.getContent());
            board.setUser(loginUser);
            boardRepository.save(board);
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
            if (imageName != null) {
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

    public Page<MyBoardDTO> getMyBoardsOrderByWriteDate(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MyBoardDTO> myBoards = boardRepository.findMyBoardsOrderByWriteDate(user, pageRequest);
        for (MyBoardDTO myBoard : myBoards) {
            Integer commentCount = commentRepository.countByBoardId(myBoard.getId());
            myBoard.setCommentCount(commentCount);
        }
        return myBoards;
    }

    public Page<MyBoardDTO> getMyBoardsOrderByViewCount(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MyBoardDTO> myBoards = boardRepository.findMyBoardsOrderByViewCount(user, pageRequest);
        for (MyBoardDTO myBoard : myBoards) {
            Integer commentCount = commentRepository.countByBoardId(myBoard.getId());
            myBoard.setCommentCount(commentCount);
        }
        return myBoards;
    }

    public Page<MyBookmarkDTO> getMyBookmarks(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MyBookmarkDTO> myBookmarks = boardRepository.findBookmark(user, pageRequest);
        for (MyBookmarkDTO myBookmark : myBookmarks) {
            Integer commentCount = commentRepository.countByBoardId(myBookmark.getId());
            myBookmark.setCommentCount(commentCount);
        }
        return myBookmarks;
    }
}