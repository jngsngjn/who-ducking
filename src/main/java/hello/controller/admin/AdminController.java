package hello.controller.admin;

import hello.dto.admin.*;
import hello.entity.user.User;
import hello.service.admin.AdminService;
import hello.service.admin.ExcelService;
import hello.service.playground.PrizeService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

import static hello.entity.request.RequestStatus.RECEIVED;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PrizeService prizeService;
    private final ExcelService excelService;

    @GetMapping
    public String adminPage() {
        return "admin/adminPage";
    }

    @GetMapping("/user-info")
    public String userInfoPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<UserInfoDTO> userPage = adminService.getUserInfoPage(page, 10);
        model.addAttribute("userPage", userPage);
        return "admin/userInfo";
    }

    @GetMapping("/add-animation")
    public String addAniPage() {
        return "admin/addAnimation";
    }

    @PostMapping("/add-animation")
    public String addAnimation(@ModelAttribute AnimationDTO animationDTO) throws IOException {
        adminService.saveAnimation(animationDTO);
        return "redirect:/admin";
    }

    @PostMapping("/add-animation/excel")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        excelService.importAnimationsFromExcel(file);
        return "redirect:/admin";
    }

    @GetMapping("/request-list")
    public String requestList(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<RequestListDTO> requestPage = adminService.getRequestsByStatus(RECEIVED, page, 10);
        model.addAttribute("requestPage", requestPage);
        return "admin/requestList";
    }

    @GetMapping("/request/{id}")
    public String viewRequest(@PathVariable(name = "id") Long id, Model model) {
        RequestDetailDTO requestDetail = adminService.getRequestById(id);
        model.addAttribute("requestDetail", requestDetail);
        return "admin/requestDetail";
    }

    @PostMapping("/request/{id}/approve")
    public String approveRequest(@PathVariable(name = "id") Long id,
                                 @ModelAttribute AnimationDTO animationDTO) throws IOException {
        adminService.approveRequest(id);
        adminService.saveAnimation(animationDTO);
        return "redirect:/admin/request-list";
    }

    @PostMapping("/request/{id}/reject")
    public String rejectRequest(@PathVariable(name = "id") Long id,
                                @ModelAttribute RequestRejectDTO requestRejectDTO) {
        requestRejectDTO.setId(id);
        adminService.rejectRequest(requestRejectDTO);
        return "redirect:/admin/request-list";
    }

    @GetMapping("/prize")
    public String prizePage(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<AdminPrizeListDTO> currentPrizes = adminService.getCurrentPrizes(page, 10);
        Page<AdminPrizeListDTO> expiredPrizes = adminService.getExpiredPrizes(page, 10);
        model.addAttribute("currentPrizes", currentPrizes);
        model.addAttribute("expiredPrizes", expiredPrizes);
        return "admin/adminPrize";
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
    public boolean drawUser(@RequestParam("prizeId") Long prizeId) throws MessagingException, URISyntaxException, IOException {
        User user = prizeService.randomDraw(prizeId);
        return user != null;
    }

    @GetMapping("/announcement")
    public String announcementPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<AnnouncementListDTO> announcementPage = adminService.getAnnouncementPage(page, 5);
        model.addAttribute("announcements", announcementPage);
        return "admin/announcement";
    }

    @PostMapping("/announcement")
    public String writeAnnouncement(@ModelAttribute AnnouncementWriteDTO announcementWriteDTO) {
        adminService.addAnnouncement(announcementWriteDTO);
        return "redirect:/admin/announcement";
    }

    @PostMapping("/announcement-delete")
    public String deleteAnnouncement(@RequestParam("id") Long id) {
        adminService.deleteAnnouncement(id);
        return "redirect:/admin/announcement";
    }

    @GetMapping("/popup-store")
    public String popupStorePage() {
        return "admin/popupStore-admin";
    }

    @PostMapping("/popup-store/add")
    public String popupStoreAdd(@ModelAttribute PopupStoreAddDTO popupStoreAddDTO) throws IOException {
        System.out.println("popupStoreAddDTO = " + popupStoreAddDTO);
        adminService.addPopupStore(popupStoreAddDTO);
        return "redirect:/admin/popup-store";
    }
}