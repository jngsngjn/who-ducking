package hello.controller.playground;

import hello.dto.playground.prize.PrizeBasicDTO;
import hello.service.playgroud.PrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static hello.entity.prize.PrizeGrade.*;

@Controller
@RequiredArgsConstructor
public class LuckyDrawListController {

    private final PrizeService prizeService;

    @GetMapping("/playground/lucky-draw")
    public String luckyDraw(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "type", defaultValue = "UR") String type) {

        List<PrizeBasicDTO> ur = prizeService.getPrizePage(UR);
        List<PrizeBasicDTO> sr = prizeService.getPrizePage(SR);
        List<PrizeBasicDTO> r = prizeService.getPrizePage(R);
        List<PrizeBasicDTO> n = prizeService.getPrizePage(N);

        model.addAttribute("ur", ur);
        model.addAttribute("sr", sr);
        model.addAttribute("r", r);
        model.addAttribute("n", n);
        model.addAttribute("currentType", type);

        return "luckyDrawList";
    }
}