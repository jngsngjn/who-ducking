package hello.service.search;

import hello.dto.search.SearchAnimationDTO;
import hello.dto.search.SearchAnnouncementDTO;
import hello.dto.search.SearchBoardDTO;
import hello.entity.animation.Animation;
import hello.repository.db.AnimationRepository;
import hello.repository.db.AnnouncementRepository;
import hello.repository.db.BoardRepository;
import hello.repository.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        String processedName = name.replace(" ", "");
        return animationRepository.findByNameIgnoringSpaces(processedName)
                .stream()
                .map(this::getSearchAnimationDTO)
                .collect(Collectors.toList());
    }

    public Page<SearchAnimationDTO> searchAnimationsPage(String name, int page, int size) {
        String processedName = name.replace(" ", "");
        PageRequest pageRequest = PageRequest.of(page, size);
        return animationRepository.findByNameIgnoringSpaces(processedName, pageRequest)
                .map(this::getSearchAnimationDTO);
    }

    private @NotNull SearchAnimationDTO getSearchAnimationDTO(Animation animation) {
        SearchAnimationDTO dto = new SearchAnimationDTO(animation.getId(), animation.getName(), animation.getImageName());
        int reviewCount = reviewRepository.findReviewCount(animation);
        double reviewScore = reviewRepository.findReviewScore(animation);
        dto.setReviewCount(reviewCount);
        dto.setScore(reviewScore);
        return dto;
    }

    public List<SearchBoardDTO> searchBoardList(String name) {
        return boardRepository.findByTitleOrContentContaining(name);
    }

    public Page<SearchBoardDTO> searchBoardPage(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return boardRepository.findByTitleOrContentContainingPage(name, pageRequest);
    }

    public List<SearchAnnouncementDTO> searchAnnouncementList(String name) {
        return announcementRepository.findByTitleOrContentContaining(name);
    }

    public Page<SearchAnnouncementDTO> searchAnnouncementList(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return announcementRepository.findByTitleOrContentContainingPage(name, pageRequest);
    }
}