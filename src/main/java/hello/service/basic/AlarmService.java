package hello.service.basic;

import hello.entity.alarm.Alarm;
import hello.entity.request.Request;
import hello.entity.user.User;
import hello.repository.db.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hello.entity.alarm.AlarmType.REQUEST;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

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
}