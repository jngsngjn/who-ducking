package hello.service.main;

import hello.dto.main.MainDTO;
import hello.repository.db.AnimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final AnimationRepository animationRepository;

    public List<MainDTO> getMainAnimations() {
        List<Object[]> animations = animationRepository.findLatestAnimations();
        return animations.stream()
                .map(animation -> new MainDTO((Long) animation[0], (String) animation[1], (String) animation[2]))
                .collect(Collectors.toList());
    }
}
