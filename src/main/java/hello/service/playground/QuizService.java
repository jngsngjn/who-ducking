package hello.service.playground;

import hello.dto.playground.QuizDTO;
import hello.repository.db.AnimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final AnimationRepository animationRepository;

    public List<QuizDTO> getRandomQuizzes() {
        List<Object[]> quizzes = animationRepository.findRandomQuizzes();
        return quizzes.stream()
                .map(quiz -> new QuizDTO((String) quiz[0], (String) quiz[1]))
                .collect(Collectors.toList());
    }
}