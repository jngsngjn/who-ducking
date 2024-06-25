package hello.controller.playground;

import hello.dto.playground.PointsRequest;
import hello.dto.user.CustomOAuth2User;
import hello.entity.user.User;
import hello.service.basic.PointService;
import hello.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class RouletteController {

    private final PointService pointService;
    private final UserService userService;

    @GetMapping("/playground/roulette")
    public String roulettePage() {
        return "playground/roulette";
    }

    @PostMapping("/roulette/get-points")
    @ResponseBody
    public String updatePoints(@AuthenticationPrincipal CustomOAuth2User user,
                               @RequestBody PointsRequest pointsRequest) {
        User loginUser = userService.getLoginUserDetail(user);
        pointService.increasePoint(loginUser, pointsRequest.getPoints());
        return "{\"success\": true}";
    }

    @PostMapping("/roulette/check-points")
    @ResponseBody
    public boolean checkPoints(@AuthenticationPrincipal CustomOAuth2User user) {
        User loginUser = userService.getLoginUserDetail(user);
        int points = loginUser.getPoint();
        int wheelPoint = 2;
        boolean result = points >= wheelPoint;
        if (result) {
            pointService.decreasePoint(loginUser, wheelPoint);
        }
        return result;
    }
}