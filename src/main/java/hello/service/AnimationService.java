package hello.service;


import hello.dto.animation.GetAniListDTO;
import hello.repository.AnimationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnimationService {

    private final AnimationRepository animationRepository;

    @Autowired
    public AnimationService(AnimationRepository animationRepository) {
        this.animationRepository = animationRepository;
    }

    public List<GetAniListDTO> getAllAnimationWithReviewData() {
        return animationRepository.findAnimationsWithReviews();
    }
}
