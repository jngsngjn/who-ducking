package hello.service.basic;

import hello.entity.alarm.Alarm;
import hello.entity.animation.Animation;
import hello.entity.board.Board;
import hello.entity.genre.Genre;
import hello.entity.genre.UserGenre;
import hello.entity.request.Request;
import hello.entity.user.User;
import hello.repository.db.AlarmRepository;
import hello.repository.db.UserGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hello.entity.alarm.AlarmType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserGenreRepository userGenreRepository;

    public void requestAlarmService(Request request) {
        User user = request.getUser();
        Alarm alarm = new Alarm();

        alarm.setAlarmType(REQUEST);
        alarm.setUser(user);
        alarm.setRequest(request);
        alarmRepository.save(alarm);
    }

    public int getUserAlarmCount(User user) {
        return alarmRepository.findCountByUser(user);
    }

    public List<Alarm> getUserAlarms(User user) {
        return alarmRepository.findAllByUser(user);
    }

    public void deleteAlarm(Long id) {
        alarmRepository.deleteById(id);
    }

    public void deleteAllAlarms(Long userId) {
        alarmRepository.deleteByUserId(userId);
    }

    public void animationAlarmService(List<Genre> genres, Animation animation) {
        List<UserGenre> userGenres = userGenreRepository.findByGenreIn(genres);
        List<User> users = userGenres.stream()
                .map(UserGenre::getUser)
                .distinct()
                .toList();

        users.forEach(user -> {
            Alarm alarm = new Alarm();
            alarm.setAnimation(animation);
            alarm.setAlarmType(ANIMATION);
            alarm.setUser(user);
            alarmRepository.save(alarm);
        });
    }

    public void commentAlarmService(Board board) {
        User user = board.getUser();
        if (user != null) {
            Alarm alarm = new Alarm();

            alarm.setAlarmType(COMMENT);
            alarm.setUser(user);
            alarm.setBoard(board);
            alarmRepository.save(alarm);
        }
    }
}