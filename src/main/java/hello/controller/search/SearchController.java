package hello.controller.search;

import hello.dto.search.SearchAnimationDTO;
import hello.dto.search.SearchAnnouncementDTO;
import hello.dto.search.SearchBoardDTO;
import hello.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public String search(@RequestParam String name, Model model) {
        List<SearchAnimationDTO> animations = searchService.searchAnimations(name);
        List<SearchBoardDTO> boards = searchService.searchBoards(name);
        List<SearchAnnouncementDTO> announcements = searchService.searchAnnouncements(name);
        model.addAttribute("animations", animations);
        model.addAttribute("boards", boards);
        model.addAttribute("announcements", announcements);
        return "search/searchAll";
    }
}