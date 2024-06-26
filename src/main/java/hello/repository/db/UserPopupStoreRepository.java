package hello.repository.db;

import hello.entity.popup.PopupStore;
import hello.entity.popup.UserPopupStore;
import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPopupStoreRepository extends JpaRepository<UserPopupStore, Long> {

    boolean existsByUserAndPopupStore(User user, PopupStore popupStore);

    UserPopupStore findByUserAndPopupStore(User user, PopupStore popupStore);
}