package hello.service.basic;

import hello.entity.user.LoginHistory;
import hello.entity.user.User;
import hello.repository.db.LoginHistoryRepository;
import hello.repository.db.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final PointService pointService;
    private final ExpService expService;
    private final UserService userService;

    public void processLogin(User user) {
        LocalDate today = LocalDate.now();

        // 오늘 이미 로그인 했는지 확인
        Optional<LoginHistory> loginHistoryOpt = loginHistoryRepository.findByUserAndLoginDate(user, today);
        if (!loginHistoryOpt.isPresent()) {
            // 로그인 기록 추가
            loginHistoryRepository.save(new LoginHistory(user, today));

            // 연속 출석 체크
            int consecutiveDays = checkConsecutiveDays(user, today);

            if (consecutiveDays == 7) {
                // 7일 연속 출석 포인트 추가
                pointService.increasePoint(user, 3); // 7일째에는 3포인트
            } else {
                // 일반 출석 포인트 추가
                pointService.increasePoint(user, 1); // 하루 1포인트
            }

            if (!userService.isLevelOne(user)) {
                expService.increaseExp(user, 5);
            }
            userRepository.save(user);
        }
    }

    private int checkConsecutiveDays(User user, LocalDate today) {
        int consecutiveDays = 1; // 기본적으로 오늘을 포함하여 1일로 설정
        LocalDate dateToCheck = today.minusDays(1); // 어제 날짜부터 체크 시작

        // 어제부터 과거로 돌아가며 로그인 기록을 확인
        while (loginHistoryRepository.findByUserAndLoginDate(user, dateToCheck).isPresent()) {
            consecutiveDays++; // 연속 출석일 수 증가
            dateToCheck = dateToCheck.minusDays(1); // 하루 전으로 이동
        }

        return consecutiveDays; // 최종 연속 출석일 수 반환
    }
}