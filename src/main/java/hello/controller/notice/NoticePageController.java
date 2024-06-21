package hello.controller.notice;

import hello.dto.notice.NoticeDTO;
import hello.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticePageController {

    private final NoticeService noticeService;

    @GetMapping
    public String notice(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<NoticeDTO> noticePage = noticeService.getNoticePage(page, 5);
        model.addAttribute("notices", noticePage);
        return "/notice/noticePage";
    }
}