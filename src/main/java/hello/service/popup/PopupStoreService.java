package hello.service.popup;

import hello.entity.popup.PopupStore;
import hello.entity.popup.UserPopupStore;
import hello.entity.user.User;
import hello.repository.db.PopupStoreRepository;
import hello.repository.db.UserPopupStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopupStoreService {

    private final PopupStoreRepository popupStoreRepository;
    private final UserPopupStoreRepository userPopupStoreRepository;

    public List<PopupStore> getAllPopupStores() {
        return popupStoreRepository.findAll();
    }

    public Optional<PopupStore> getPopupStore(Long id) {
        return popupStoreRepository.findById(id);
    }

    public UserPopupStore getPopupStoreByUserAndPopupStore(User user, PopupStore popupStore) {
        return userPopupStoreRepository.findByUserAndPopupStore(user, popupStore);
    }

    public boolean alreadyBookmark(User user, PopupStore popupStore) {
        return userPopupStoreRepository.existsByUserAndPopupStore(user, popupStore);
    }

    public void bookmark(UserPopupStore userPopupStore) {
        userPopupStoreRepository.save(userPopupStore);
    }

    public void deleteBookmark(UserPopupStore userPopupStore) {
        userPopupStoreRepository.delete(userPopupStore);
    }

    public List<UserPopupStore> getUserPopupStore(User user) {
        return userPopupStoreRepository.findAllByUser(user);
    }
}