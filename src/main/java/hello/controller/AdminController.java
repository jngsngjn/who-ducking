package hello.controller;

import hello.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String userInfoPage(Model model) {

        return "admin/userInfo";
    }
}