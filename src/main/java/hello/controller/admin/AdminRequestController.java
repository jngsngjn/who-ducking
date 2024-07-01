package hello.controller.admin;

import hello.dto.admin.AnimationDTO;
import hello.dto.admin.RequestDetailDTO;
import hello.dto.admin.RequestListDTO;
import hello.dto.admin.RequestRejectDTO;
import hello.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static hello.entity.request.RequestStatus.RECEIVED;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRequestController {

    private final AdminService adminService;

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
}