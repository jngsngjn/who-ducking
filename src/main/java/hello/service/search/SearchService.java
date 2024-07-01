package hello.service.search;

import hello.dto.search.SearchAnimationDTO;
import hello.dto.search.SearchAnnouncementDTO;
import hello.dto.search.SearchBoardDTO;
import hello.repository.db.AnimationRepository;
import hello.repository.db.AnnouncementRepository;
import hello.repository.db.BoardRepository;
import hello.repository.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final AnimationRepository animationRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;
    private final AnnouncementRepository announcementRepository;

    public List<SearchAnimationDTO> searchAnimations(String name) {
        return animationRepository.findByNameContaining(name)
                .stream()
                .map(animation -> {
                    SearchAnimationDTO dto = new SearchAnimationDTO(animation.getId(), animation.getName(), animation.getImageName());
                    int reviewCount = reviewRepository.findReviewCount(animation);
                    double reviewScore = reviewRepository.findReviewScore(animation);
                    dto.setReviewCount(reviewCount);
                    dto.setScore(reviewScore);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<SearchBoardDTO> searchBoards(String name) {
        return boardRepository.findByTitleOrContentContaining(name);
    }

    public List<SearchAnnouncementDTO> searchAnnouncements(String name) {
        return announcementRepository.findByTitleOrContentContaining(name);
    }
}