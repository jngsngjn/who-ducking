package hello.controller.playground;

import hello.dto.playground.WorldCupDTO;
import hello.service.playground.WorldCupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WorldCupController {

    private final WorldCupService worldCupService;

    @GetMapping("/playground/world-cup")
    public String world() {
        return "playground/worldCup";
    }

    @GetMapping("/api/world-cup")
    @ResponseBody
    public List<WorldCupDTO> getWorldCupAnimations() {
        return worldCupService.getWorldCupAnimations();
    }
}