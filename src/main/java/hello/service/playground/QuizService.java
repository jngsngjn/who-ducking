package hello.service.playground;

import hello.dto.playground.WorldCupDTO;
import hello.repository.db.AnimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final AnimationRepository animationRepository;

    public List<WorldCupDTO> getWorldCupAnimations() {
        List<WorldCupDTO> animations = animationRepository.findWorldCupAnimations();
        Collections.shuffle(animations); // 데이터를 셔플
        return animations;
    }


}