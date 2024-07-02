package hello.controller.admin;

import hello.dto.admin.AdminPrizeAddDTO;
import hello.dto.admin.AdminPrizeListDTO;
import hello.dto.admin.PrizeDrawDTO;
import hello.entity.user.User;
import hello.service.admin.AdminService;
import hello.service.playground.PrizeService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPrizeController {

    private final AdminService adminService;
    private final PrizeService prizeService;

    @GetMapping("/prize")
    public String prizePage(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<AdminPrizeListDTO> currentPrizes = adminService.getCurrentPrizes(page, 10);
        model.addAttribute("currentPrizes", currentPrizes);
        return "admin/adminPrize";
    }

    @GetMapping("/prize/expired")
    public String prizeExpiredPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<AdminPrizeListDTO> expiredPrizes = adminService.getExpiredPrizes(page, 10);
        model.addAttribute("expiredPrizes", expiredPrizes);
        return "admin/adminPrizeExpired";
    }

    @PostMapping("/add-prize")
    public String addPrize(@ModelAttribute AdminPrizeAddDTO adminPrizeAddDTO) throws IOException {
        adminService.addPrize(adminPrizeAddDTO);
        return "redirect:/admin/prize";
    }

    @GetMapping("/prize/draw/{prizeId}")
    public String drawPage(@PathVariable("prizeId") Long prizeId,
                           Model model) {
        PrizeDrawDTO prizeDraw = adminService.getPrizeDraw(prizeId);
        model.addAttribute("prizeDraw", prizeDraw);
        return "admin/adminPrizeDraw";
    }

    @ResponseBody
    @PostMapping("/prize-draw/random")
    public String drawUser(@RequestParam("prizeId") Long prizeId) throws MessagingException, URISyntaxException, IOException {
        User user = prizeService.randomDraw(prizeId);
        if (user == null) {
            return "fail";
        } else {
            return user.getNickname();
        }
    }
}