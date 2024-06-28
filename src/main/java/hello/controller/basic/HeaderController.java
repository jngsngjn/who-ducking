package hello.controller.basic;

import hello.dto.user.AlarmDTO;
import hello.dto.user.CustomOAuth2User;
import hello.dto.user.HeaderDTO;
import hello.entity.alarm.Alarm;
import hello.entity.alarm.AlarmType;
import hello.entity.user.User;
import hello.service.basic.AlarmService;
import hello.service.basic.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HeaderController {

    private final UserService userService;
    private final AlarmService alarmService;

    @PostMapping("/update-header")
    public HeaderDTO handleRequest(@AuthenticationPrincipal CustomOAuth2User user, HttpSession session) {
        if (user != null) {
            User loginUser = userService.getLoginUserDetail(user);

            Long level = loginUser.getLevel().getId();
            int point = loginUser.getPoint();
            int currentExp = loginUser.getCurrentExp();
            int maxExp = loginUser.getLevel().getMaxExp();

            session.setAttribute("level", level);
            session.setAttribute("point", point);
            session.setAttribute("currentExp", currentExp);
            session.setAttribute("maxExp", maxExp);

            return new HeaderDTO(point);
        }
        return null;
    }

    @PostMapping("/update-alarm")
    public AlarmDTO updateAlarm(@AuthenticationPrincipal CustomOAuth2User user) {
        if (user != null) {
            User loginUser = userService.getLoginUserDetail(user);

            int alarmCount = alarmService.getUserAlarmCount(loginUser);
            List<Alarm> userAlarms = alarmService.getUserAlarms(loginUser);
            List<AlarmDTO.AlarmResponse> alarmResponses = userAlarms.stream().map(alarm -> {
                String message = "";
                String link = "";
                if (alarm.getAlarmType() == AlarmType.REQUEST) {
                    message = "나의 건의 내역 상태가 업데이트 되었습니다.";
                    link = "/myPage/request-list?page=0&requestId=" + alarm.getRequest().getId();
                }
                else if (alarm.getAlarmType() == AlarmType.ANIMATION){
                    message = "선호 장르의 새로운 애니가 추가되었습니다.";
                    link = "/animations/" + alarm.getAnimation().getId();
                }
                return new AlarmDTO.AlarmResponse(alarm.getId(), message, link);
            }).collect(Collectors.toList());

            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setAlarmCount(alarmCount);
            alarmDTO.setAlarms(alarmResponses);
            return alarmDTO;
        }
        return null;
    }

    @PostMapping("/delete-alarm")
    public ResponseEntity<Void> deleteAlarm(@RequestParam Long id) {
        try {
            alarmService.deleteAlarm(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/delete-all-alarms")
    public ResponseEntity<Void> deleteAllAlarms(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        User loginUser = userService.getLoginUserDetail(oAuth2User);
        try {
            alarmService.deleteAllAlarms(loginUser.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}