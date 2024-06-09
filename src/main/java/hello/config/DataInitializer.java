package hello.config;

import hello.entity.genre.Genre;
import hello.entity.user.Level;
import hello.repository.GenreRepository;
import hello.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final LevelRepository levelRepository;
    private final GenreRepository genreRepository;

    private boolean initialized = false;

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
            levels.add(new Level(0, ""));
            levels.add(new Level(20, ""));
            levels.add(new Level(40, ""));
            levels.add(new Level(100, ""));
            levels.add(new Level(240, ""));
            levels.add(new Level(580, ""));
            levels.add(new Level(1300, ""));
            levels.add(new Level(3200, ""));
            levels.add(new Level(8700, ""));
            levels.add(new Level(20000, ""));

            levelRepository.saveAll(levels);
        }
    }
}