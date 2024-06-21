package hello.service.notice;

import hello.dto.notice.NoticeDTO;
import hello.repository.db.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final AnnouncementRepository announcementRepository;

    public Page<NoticeDTO> getNoticePage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return announcementRepository.findNoticePage(pageable);
    }
}