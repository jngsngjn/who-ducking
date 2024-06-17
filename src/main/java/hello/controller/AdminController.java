package hello.controller;

import hello.dto.admin.UserInfoDTO;
import hello.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

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
}