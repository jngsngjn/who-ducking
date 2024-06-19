package hello.service;

import hello.entity.user.Level;
import hello.entity.user.User;
import hello.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpService {

    private final LevelRepository levelRepository;

    public void increaseExp(User user, int exp) {
        int currentExp = user.getCurrentExp();
        int totalExp = currentExp + exp;
        int maxExp = user.getLevel().getMaxExp();

        while (totalExp >= maxExp) {
            levelUp(user);
            totalExp -= maxExp;
            maxExp = user.getLevel().getMaxExp();
        }

        user.setCurrentExp(totalExp);
    }

    public void levelUp(User user) {
        Long currentLevel = user.getLevel().getId();

        if (currentLevel < 10) {
            long nextLevel = currentLevel + 1L;
            Level nextLevelEntity = levelRepository.findById(nextLevel)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid level id: " + nextLevel));
            user.setLevel(nextLevelEntity);
        }
    }
}