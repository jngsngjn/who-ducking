package hello.service.popup;

import hello.entity.popup.PopupStore;
import hello.repository.db.PopupStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopupStoreService {

    private final PopupStoreRepository popupStoreRepository;

    public List<PopupStore> getAllPopupStores() {
        return popupStoreRepository.findAll();
    }
}