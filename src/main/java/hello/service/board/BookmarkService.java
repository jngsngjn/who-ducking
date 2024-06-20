package hello.service.board;

import hello.entity.board.Board;
import hello.entity.board.Bookmark;
import hello.entity.user.User;
import hello.repository.BookmarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public void addBookmark(Bookmark bookmark) {
        bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Long userId, Long boardId) {
        bookmarkRepository.deleteByUserIdAndBoardId(userId, boardId);
    }

    public boolean isBookmarked(User user, Board board) {
        return bookmarkRepository.existsByUserAndBoard(user, board);
    }
}
