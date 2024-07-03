package hello.controller.search;

import hello.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class SearchRestController {

    private final SearchService searchService;

    @GetMapping("/page-number")
    public ResponseEntity<Integer> getPageNumber(@RequestParam("id") Long id) {
        int pageSize = 5;
        int pageNumber = searchService.findAnnouncementPage(id, pageSize);
        return ResponseEntity.ok(pageNumber - 1);
    }
}