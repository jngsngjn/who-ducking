package hello.repository.db;

import hello.dto.admin.AnnouncementListDTO;
import hello.dto.notice.NoticeDTO;
import hello.dto.search.SearchAnnouncementDTO;
import hello.entity.board.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("select new hello.dto.admin.AnnouncementListDTO(a.id, a.title, a.writeDate) from Announcement a order by a.id desc")
    Page<AnnouncementListDTO> findAnnouncementPage(Pageable pageable);

    @Query("select new hello.dto.notice.NoticeDTO(a.id, a.title, a.content, a.writeDate) from Announcement a order by a.id desc")
    Page<NoticeDTO> findNoticePage(Pageable pageable);

    @Query("SELECT new hello.dto.search.SearchAnnouncementDTO(a.id, a.title, a.content) FROM Announcement a WHERE a.title LIKE %:name% OR a.content LIKE %:name% order by a.id desc")
    List<SearchAnnouncementDTO> findByTitleOrContentContaining(@Param("name") String name);

    @Query("SELECT new hello.dto.search.SearchAnnouncementDTO(a.id, a.title, a.content) FROM Announcement a WHERE a.title LIKE %:name% OR a.content LIKE %:name% order by a.id desc")
    Page<SearchAnnouncementDTO> findByTitleOrContentContainingPage(@Param("name") String name, Pageable pageable);
}