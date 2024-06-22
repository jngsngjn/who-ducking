package hello.controller.playground;

import hello.dto.playground.prize.PrizeBasicDTO;
import hello.service.playgroud.PrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static hello.entity.prize.PrizeGrade.*;

@Controller
@RequiredArgsConstructor
public class PlaygroundController {

    private final PrizeService prizeService;

    @GetMapping("/playground")
    public String playground(Model model) {
        PrizeBasicDTO ur = prizeService.getEarliestPrizeByGrade(UR);
        PrizeBasicDTO sr = prizeService.getEarliestPrizeByGrade(SR);
        PrizeBasicDTO r = prizeService.getEarliestPrizeByGrade(R);

        model.addAttribute("ur", ur);
        model.addAttribute("sr", sr);
        model.addAttribute("r", r);
        return "playground";
    }
}