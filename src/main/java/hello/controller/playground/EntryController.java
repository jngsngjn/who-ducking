package hello.controller.playground;

import hello.dto.user.CustomOAuth2User;
import hello.entity.prize.Prize;
import hello.entity.user.User;
import hello.service.basic.UserService;
import hello.service.playgroud.PrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EntryController {

    private final UserService userService;
    private final PrizeService prizeService;

    // 경품 응모 가능한지 확인
    @PostMapping("/check-user/{prizeId}")
    public ResponseEntity<Map<String, Boolean>> checkEntry(@AuthenticationPrincipal CustomOAuth2User user,
                                                           @PathVariable("prizeId") Long prizeId) {
        User loginUser = userService.getLoginUserDetail(user);

        Prize prize = prizeService.findById(prizeId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("alreadyEntry", prizeService.alreadyEntry(loginUser));
        response.put("validGradeAndLevel", prizeService.checkPrizeGradeAndLevel(prize, loginUser));
        response.put("hasEnoughPoints", prizeService.checkPoint(loginUser));
        response.put("addressEmpty", prizeService.isAddressEmpty(loginUser));

        return ResponseEntity.ok(response);
    }

    // 경품 응모
    @PostMapping("/entry-prize/{prizeId}")
    public ResponseEntity<String> enterPrize(@AuthenticationPrincipal CustomOAuth2User user,
                                             @PathVariable("prizeId") Long prizeId) {
        User loginUser = userService.getLoginUserDetail(user);

        try {
            prizeService.entryPrize(prizeId, loginUser);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
            return new ResponseEntity<>("응모가 완료되었습니다.", headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("응모 처리 중 오류가 발생했습니다.");
        }
    }
}