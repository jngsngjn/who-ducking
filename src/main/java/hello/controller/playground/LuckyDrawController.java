package hello.controller.playground;

import hello.dto.playground.prize.PrizeBasicDTO;
import hello.service.playgroud.PrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static hello.entity.prize.PrizeGrade.*;

@Controller
@RequiredArgsConstructor
public class LuckyDrawController {

    private final PrizeService prizeService;

    @GetMapping("/playground/lucky-draw")
    public String luckyDraw(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "type", defaultValue = "UR") String type) {
        int size = 6;

        Page<PrizeBasicDTO> ur = prizeService.getPrizePage(page, size, UR);
        Page<PrizeBasicDTO> sr = prizeService.getPrizePage(page, size, SR);
        Page<PrizeBasicDTO> r = prizeService.getPrizePage(page, size, R);
        Page<PrizeBasicDTO> n = prizeService.getPrizePage(page, size, N);

        model.addAttribute("ur", ur);
        model.addAttribute("sr", sr);
        model.addAttribute("r", r);
        model.addAttribute("n", n);
        model.addAttribute("currentType", type);

        return "luckyDraw";
    }
}