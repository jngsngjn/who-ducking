package hello.controller.admin;

import hello.dto.admin.AnnouncementListDTO;
import hello.dto.admin.AnnouncementWriteDTO;
import hello.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAnnouncementController {

    private final AdminService adminService;

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
}