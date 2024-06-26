package hello.controller.popup;

import hello.dto.popup.PopupStoreDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.popup.PopupStore;
import hello.entity.popup.UserPopupStore;
import hello.entity.user.User;
import hello.service.basic.UserService;
import hello.service.popup.PopupStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PopupStoreApiController {

    private final PopupStoreService popupStoreService;
    private final UserService userService;

    @GetMapping("/api/popup-stores")
    public ResponseEntity<List<PopupStoreDTO>> getAllPopupStores(@AuthenticationPrincipal CustomOAuth2User user) {
        LocalDate today = LocalDate.now();
        List<PopupStore> allStores = popupStoreService.getAllPopupStores().stream()
                .filter(store -> !store.getEndDate().isBefore(today))
                .sorted(Comparator.comparing(PopupStore::getStartDate))
                .collect(Collectors.toList());

        List<PopupStoreDTO> response = new ArrayList<>();

        if (user == null) {
            // 미인증 사용자의 경우 북마크 정보를 포함하지 않고 반환
            response = allStores.stream()
                    .map(store -> new PopupStoreDTO(store, false))
                    .collect(Collectors.toList());
        } else {
            User loginUser = userService.getLoginUserDetail(user);
            List<UserPopupStore> userBookmarks = popupStoreService.getUserPopupStore(loginUser);
            response = allStores.stream()
                    .map(store -> {
                        boolean isBookmarked = userBookmarks.stream()
                                .anyMatch(bookmark -> bookmark.getPopupStore().getId().equals(store.getId()));
                        return new PopupStoreDTO(store, isBookmarked);
                    })
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(response);
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