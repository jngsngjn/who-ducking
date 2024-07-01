package hello.controller.admin;

import hello.dto.admin.PopupStoreAddDTO;
import hello.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPopupStoreController {

    private final AdminService adminService;

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