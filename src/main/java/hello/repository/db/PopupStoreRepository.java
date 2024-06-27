package hello.repository.db;

import hello.entity.popup.PopupStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupStoreRepository extends JpaRepository<PopupStore, Long> {

}