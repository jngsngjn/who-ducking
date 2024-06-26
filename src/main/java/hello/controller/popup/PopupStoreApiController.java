package hello.controller.popup;

import hello.entity.popup.PopupStore;
import hello.service.popup.PopupStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/popupstores")
@RequiredArgsConstructor
public class PopupStoreApiController {

    private final PopupStoreService popupStoreService;

    @GetMapping
    public List<PopupStore> getAllPopupStores() {
        return popupStoreService.getAllPopupStores();
    }
}