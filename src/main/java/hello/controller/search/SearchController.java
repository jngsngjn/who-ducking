package hello.controller.search;

import hello.dto.search.SearchAnimationDTO;
import hello.dto.search.SearchAnnouncementDTO;
import hello.dto.search.SearchBoardDTO;
import hello.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public String searchAll(@RequestParam String name, Model model) {
        List<SearchAnimationDTO> animations = searchService.searchAnimations(name);
        List<SearchBoardDTO> boards = searchService.searchBoardList(name);
        List<SearchAnnouncementDTO> announcements = searchService.searchAnnouncementList(name);
        model.addAttribute("animations", animations);
        model.addAttribute("boards", boards);
        model.addAttribute("announcements", announcements);
        model.addAttribute("name", name);
        return "search/searchAll";
    }

    @GetMapping("/board")
    public String searchBoard(@RequestParam String name, Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<SearchBoardDTO> boards = searchService.searchBoardPage(name, page, 10);
        model.addAttribute("boards", boards);
        model.addAttribute("name", name);
        return "search/searchFreeBoard";
    }
}