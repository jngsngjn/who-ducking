package hello.repository.db;

import hello.dto.admin.AnnouncementListDTO;
import hello.dto.notice.NoticeDTO;
import hello.entity.board.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("select new hello.dto.admin.AnnouncementListDTO(a.id, a.title, a.writeDate) from Announcement a")
    Page<AnnouncementListDTO> findAnnouncementPage(Pageable pageable);

    @Query("select new hello.dto.notice.NoticeDTO(a.id, a.title, a.content, a.writeDate) from Announcement a")
    Page<NoticeDTO> findNoticePage(Pageable pageable);
}