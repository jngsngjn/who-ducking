package hello.controller.admin;

import hello.dto.admin.AnimationDTO;
import hello.service.admin.AdminService;
import hello.service.admin.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAnimationController {

    private final AdminService adminService;
    private final ExcelService excelService;

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
}