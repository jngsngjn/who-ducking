package hello.controller.playground;

import hello.dto.user.CustomOAuth2User;
import hello.entity.user.LoginHistory;
import hello.entity.user.User;
import hello.repository.db.LoginHistoryRepository;
import hello.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final UserService userService;
    private final LoginHistoryRepository loginHistoryRepository;

    @GetMapping("/calendar")
    public String calendar(@AuthenticationPrincipal CustomOAuth2User loginUser, Model model) {
        User user = userService.getLoginUserDetail(loginUser);
        model.addAttribute("user", user);
        return "playground/calendar";
    }

    @GetMapping("/get-attendance")
    @ResponseBody
    public Map<LocalDate, Boolean> getAttendance(@RequestParam Long userId) {
        User user = userService.findUserById(userId);
        Map<LocalDate, Boolean> attendanceMap = new HashMap<>();
        List<LoginHistory> loginHistories = loginHistoryRepository.findAllByUser(user);

        for (LoginHistory loginHistory : loginHistories) {
            attendanceMap.put(loginHistory.getLoginDate(), true);
        }

        return attendanceMap;
    }
}