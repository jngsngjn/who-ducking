package hello.config;

import hello.entity.genre.Genre;
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

    // 초기 데이터 삽입 시 활용
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
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
            genres.add(new Genre("이세계"));

            genreRepository.saveAll(genres);
        }
    }
}