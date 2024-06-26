package hello.controller.popup;

import hello.dto.user.CustomOAuth2User;
import hello.entity.popup.PopupStore;
import hello.entity.popup.UserPopupStore;
import hello.entity.user.User;
import hello.service.basic.UserService;
import hello.service.popup.PopupStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PopupStoreApiController {

    private final PopupStoreService popupStoreService;
    private final UserService userService;

    @GetMapping("/api/popupstores")
    public List<PopupStore> getAllPopupStores() {
        LocalDate today = LocalDate.now();
        return popupStoreService.getAllPopupStores().stream()
                .filter(store -> !store.getEndDate().isBefore(today))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/bookmark/{popupStoreId}")
    public void bookmarkPopupStore(@PathVariable("popupStoreId") Long popupStoreId, @AuthenticationPrincipal CustomOAuth2User user) {
        User loginUser = userService.getLoginUserDetail(user);
        PopupStore popupStore = popupStoreService.getPopupStore(popupStoreId).orElseThrow(() -> new RuntimeException("Popup store not found"));

        if (popupStoreService.alreadyBookmark(loginUser, popupStore)) {
            throw new RuntimeException("Already bookmarked");
        }

        UserPopupStore userPopupStore = new UserPopupStore(popupStore, loginUser);
        popupStoreService.bookmark(userPopupStore);
    }

    @DeleteMapping("/api/bookmark/{popupStoreId}")
    public void removeBookmark(@PathVariable("popupStoreId") Long popupStoreId, @AuthenticationPrincipal CustomOAuth2User user) {
        User loginUser = userService.getLoginUserDetail(user);
        PopupStore popupStore = popupStoreService.getPopupStore(popupStoreId).orElseThrow(() -> new RuntimeException("Popup store not found"));

        UserPopupStore userPopupStore = popupStoreService.getPopupStoreByUserAndPopupStore(loginUser, popupStore);
        popupStoreService.deleteBookmark(userPopupStore);
    }
}