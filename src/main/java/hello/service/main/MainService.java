package hello.service.main;

import hello.dto.main.LastedAnimationsDTO;
import hello.dto.main.PrizeMainDTO;
import hello.dto.main.RankedAnimationsDTO;
import hello.entity.animation.Animation;
import hello.repository.db.AnimationRepository;
import hello.repository.db.PrizeRepository;
import hello.repository.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ReviewRepository reviewRepository;
    private final AnimationRepository animationRepository;
    private final PrizeRepository prizeRepository;

    public List<RankedAnimationsDTO> getRankedAnimations() {
        List<RankedAnimationsDTO> animationMain = animationRepository.findRankedAnimations();

        for (RankedAnimationsDTO rankedAnimationsDTO : animationMain) {
            Animation animation = animationRepository.findById(rankedAnimationsDTO.getId()).orElse(null);
            if (animation != null) {
                double reviewScore = reviewRepository.findReviewScore(animation);
                rankedAnimationsDTO.setScore(reviewScore);
            }
        }
        return animationMain.stream()
                .sorted((dto1, dto2) -> Double.compare(dto2.getScore(), dto1.getScore())) // 스코어 내림차순 정렬
                .limit(10) // 상위 10개만 추출
                .collect(Collectors.toList());
    }

    public List<LastedAnimationsDTO> getLastedAnimations() {
        List<Object[]> animations = animationRepository.findLatestAnimations();
        return animations.stream()
                .map(animation -> new LastedAnimationsDTO((Long) animation[0], (String) animation[1], (String) animation[2]))
                .collect(Collectors.toList());
    }

    public List<PrizeMainDTO> getTop3Prizes() {
        Pageable topThree = PageRequest.of(0, 3);
        return prizeRepository.findTopPrizes(topThree);
    }
}