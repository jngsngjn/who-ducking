package hello.controller.playground;

import hello.dto.playground.prize.PrizeOneDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.prize.Prize;
import hello.entity.user.User;
import hello.service.basic.UserService;
import hello.service.playground.PrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EntryController {

    private final UserService userService;
    private final PrizeService prizeService;

    // 경품 응모 가능한지 확인
    @PostMapping("/check-user/{prizeId}")
    @ResponseBody
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
    @ResponseBody
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

    @GetMapping("/playground/lucky-draw/entry/{prizeId}")
    public String entryPage(@PathVariable("prizeId") Long prizeId, Model model) {
        PrizeOneDTO prizeOne = prizeService.getPrizeOne(prizeId);

        LocalDate endDate = prizeOne.getEndDate();
        LocalDate resultEndDate = endDate.plus(1, ChronoUnit.DAYS);
        String formattedAnnounceDate = resultEndDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"));

        List<PrizeOneDTO> randomPrizes = prizeService.getRandomPrizes(prizeId);

        model.addAttribute("prize", prizeOne);
        model.addAttribute("announceDate", formattedAnnounceDate + " 12:00");
        model.addAttribute("randomPrizes", randomPrizes);
        return "playground/luckyDrawDetail";
    }
}