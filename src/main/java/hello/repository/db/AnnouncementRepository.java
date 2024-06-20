package hello.repository.db;

import hello.dto.admin.AnnouncementListDTO;
import hello.entity.board.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("select new hello.dto.admin.AnnouncementListDTO(a.id, a.title, a.writeDate) from Announcement a")
    Page<AnnouncementListDTO> findAnnouncementPage(Pageable pageable);
}