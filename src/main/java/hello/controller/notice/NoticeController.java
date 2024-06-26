package hello.controller.notice;

import hello.dto.notice.NoticeDTO;
import hello.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/announcement")
    public String notice(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<NoticeDTO> noticePage = noticeService.getNoticePage(page, 5);
        model.addAttribute("notices", noticePage);
        return "notice/announcement";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "notice/faq";
    }
}