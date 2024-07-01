package hello.service.basic;

import hello.entity.user.Level;
import hello.entity.user.User;
import hello.repository.db.LevelRepository;
import hello.repository.db.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpService {

    private final LevelRepository levelRepository;
    private final UserRepository userRepository;

    public void increaseExp(User user, int exp, HttpSession session) {

        // 최대 레벨 : 10
        if (user.getLevel().getId().equals(10L)) {
            return;
        }

        int currentExp = user.getCurrentExp();
        int totalExp = currentExp + exp;
        int maxExp = user.getLevel().getMaxExp();

        while (totalExp >= maxExp) {
            levelUp(user, session);
            totalExp -= maxExp;
            maxExp = user.getLevel().getMaxExp();
        }

        user.setCurrentExp(totalExp);
        userRepository.save(user);
    }

    public void levelUp(User user, HttpSession session) {
        Long currentLevel = user.getLevel().getId();

        if (currentLevel < 10) {
            long nextLevel = currentLevel + 1L;
            Level nextLevelEntity = levelRepository.findById(nextLevel)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid level id: " + nextLevel));
            user.setLevel(nextLevelEntity);
            userRepository.save(user);

            if (session != null) {
                session.setAttribute("levelUp", true);
            }
        }
    }
}