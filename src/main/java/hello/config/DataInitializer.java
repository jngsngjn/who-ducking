package hello.config;

import hello.entity.genre.Genre;
import hello.entity.prize.Prize;
import hello.entity.user.Level;
import hello.repository.db.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static hello.entity.prize.PrizeGrade.*;

@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final LevelRepository levelRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final EmailCodeRepository emailCodeRepository;
    private final PrizeRepository prizeRepository;

    private boolean initialized = false;

    @Value("${level1}")
    private String level1;

    @Value("${level2}")
    private String level2;

    @Value("${level3}")
    private String level3;

    @Value("${level4}")
    private String level4;

    @Value("${level5}")
    private String level5;

    @Value("${level6}")
    private String level6;

    @Value("${level7}")
    private String level7;

    @Value("${level8}")
    private String level8;

    @Value("${level9}")
    private String level9;

    @Value("${level10}")
    private String level10;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        synchronized (this) {
            if (!initialized) {
                initializeData();
                initialized = true;
            }
        }
    }

    private void initializeData() {
        genreInitializer();
        levelInitializer();
        emailCodeInitializer();

    }

    private void emailCodeInitializer() {
        if (emailCodeRepository.count() != 0) {
            emailCodeRepository.deleteAll();
        }
    }

    private void levelInitializer() {
        if (levelRepository.count() == 0) {
            List<Level> levels = new ArrayList<>();
            levels.add(new Level(9, level1));
            levels.add(new Level(20, level2));
            levels.add(new Level(40, level3));
            levels.add(new Level(100, level4));
            levels.add(new Level(240, level5));
            levels.add(new Level(580, level6));
            levels.add(new Level(1300, level7));
            levels.add(new Level(3200, level8));
            levels.add(new Level(8700, level9));
            levels.add(new Level(20000, level10));

            levelRepository.saveAll(levels);
        }
    }

    private void genreInitializer() {
        if (genreRepository.count() == 0) {
            List<Genre> genres = new ArrayList<>();
            genres.add(new Genre("액션"));
            genres.add(new Genre("먼치킨"));
            genres.add(new Genre("추리/미스테리"));
            genres.add(new Genre("스포츠"));
            genres.add(new Genre("마법"));
            genres.add(new Genre("개그"));
            genres.add(new Genre("일상"));
            genres.add(new Genre("순정"));
            genres.add(new Genre("판타지"));
            genres.add(new Genre("학원"));
            genres.add(new Genre("힐링"));
            genres.add(new Genre("시대"));
            genres.add(new Genre("메이드"));
            genres.add(new Genre("츤데레"));
            genres.add(new Genre("얀데레"));
            genres.add(new Genre("러브"));
            genres.add(new Genre("BL"));
            genres.add(new Genre("철학"));
            genres.add(new Genre("하렘"));
            genres.add(new Genre("역하렘"));
            genres.add(new Genre("백합"));
            genres.add(new Genre("호러"));
            genres.add(new Genre("아동"));
            genres.add(new Genre("가상"));
            genres.add(new Genre("멘붕"));
            genres.add(new Genre("메카"));
            genres.add(new Genre("능력자"));
            genres.add(new Genre("쇼타"));

            genreRepository.saveAll(genres);
        }

        if (levelRepository.count() == 0) {
            List<Level> levels = new ArrayList<>();
            levels.add(new Level(9, level1));
            levels.add(new Level(20, level2));
            levels.add(new Level(40, level3));
            levels.add(new Level(100, level4));
            levels.add(new Level(240, level5));
            levels.add(new Level(580, level6));
            levels.add(new Level(1300, level7));
            levels.add(new Level(3200, level8));
            levels.add(new Level(8700, level9));
            levels.add(new Level(0, level10));

            levelRepository.saveAll(levels);
        }

        if (prizeRepository.count() == 0) {
            List<Prize> prizes = new ArrayList<>();
            prizes.add(new Prize("아이언맨 스태츄", "iron.png", SR, LocalDate.now(), LocalDate.now().plusDays(12)));
            prizes.add(new Prize("배트맨 스태츄", "batman.jpg", N, LocalDate.now(), LocalDate.now().plusDays(10)));
            prizes.add(new Prize("젤다의 전설 링크 스태츄", "rink.png", UR, LocalDate.now(), LocalDate.now().plusDays(15)));
            prizes.add(new Prize("헐크 스태츄", "hul.jpg", N, LocalDate.now(), LocalDate.now().plusDays(20)));
            prizes.add(new Prize("손흥민 피규어", "son.jpg", R, LocalDate.now(), LocalDate.now().plusDays(10)));
            prizes.add(new Prize("제드 피규어", "zed.jpg", R, LocalDate.now(), LocalDate.now().plusDays(11)));
            prizes.add(new Prize("그루트 피규어", "groot.jpg", SR, LocalDate.now(), LocalDate.now().plusDays(14)));
            prizes.add(new Prize("앤트맨 피규어", "ant.png", R, LocalDate.now(), LocalDate.now().plusDays(15)));
            prizes.add(new Prize("캡틴 아메리카 피규어", "cap.jpg", R, LocalDate.now(), LocalDate.now().plusDays(11)));
            prizes.add(new Prize("스파이더맨 피규어", "spider.jpeg", N, LocalDate.now(), LocalDate.now().plusDays(10)));

            prizeRepository.saveAll(prizes);
        }
    }
}