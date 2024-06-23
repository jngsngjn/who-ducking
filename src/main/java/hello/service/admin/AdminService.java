package hello.service.admin;

import hello.dto.admin.*;
import hello.entity.animation.Animation;
import hello.entity.board.Announcement;
import hello.entity.genre.AnimationGenre;
import hello.entity.genre.Genre;
import hello.entity.prize.Prize;
import hello.entity.request.Request;
import hello.entity.request.RequestStatus;
import hello.repository.db.*;
import hello.repository.server.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static hello.entity.request.RequestStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AnimationRepository animationRepository;
    private final FileStore fileStore;
    private final RequestRepository requestRepository;
    private final PrizeRepository prizeRepository;
    private final GenreRepository genreRepository;
    private final AnnouncementRepository announcementRepository;

    public Page<UserInfoDTO> getUserInfoPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepository.findUserInfo(pageRequest);
    }

    public void saveAnimation(AnimationDTO animationDTO) throws IOException {
        Animation animation = new Animation();
        animation.setName(animationDTO.getName());
        animation.setAuthor(animationDTO.getAuthor());
        animation.setFirstDate(animationDTO.getFirstDate());
        animation.setIsFinished(animationDTO.isFinished());
        animation.setRating(animationDTO.getRating());
        animation.setDescription(animationDTO.getDescription());

        // 장르 저장
        List<String> genres = animationDTO.getGenres();
        List<Genre> findGenres = genreRepository.findByNameIn(genres);
        findGenres.forEach(genre -> {
            AnimationGenre animationGenre = new AnimationGenre(animation, genre);
            animation.getAnimationGenres().add(animationGenre);
        });

        // 이미지 파일 저장
        MultipartFile image = animationDTO.getImage();
        if (image != null && !image.isEmpty()) {
            String filePath = fileStore.storeAnimation(image);
            animation.setImagePath(filePath);

            String imageName = filePath.substring(filePath.lastIndexOf('/') + 1);
            animation.setImageName(imageName);
        }

        animationRepository.save(animation);
    }

    public Page<RequestListDTO> getRequestsByStatus(RequestStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return requestRepository.findByStatus(status, pageable);
    }

    public RequestDetailDTO getRequestById(Long id) {
        return requestRepository.findRequestDetailById(id);
    }

    public void approveRequest(Long id) {
        Request request = requestRepository.findById(id).get();
        if (request != null) {
            request.setStatus(APPROVED);
            request.setResponseDate(LocalDate.now());
        }
    }

    public void rejectRequest(RequestRejectDTO requestRejectDTO) {
        Request request = requestRepository.findById(requestRejectDTO.getId()).get();
        if (request != null) {
            request.setResponse(requestRejectDTO.getResponse());
            request.setStatus(REJECTED);
            request.setResponseDate(LocalDate.now());
        }
    }

    public void addPrize(AdminPrizeAddDTO adminPrizeAddDTO) throws IOException {
        Prize prize = new Prize();

        prize.setName(adminPrizeAddDTO.getName());
        prize.setGrade(adminPrizeAddDTO.getGrade());
        prize.setEndDate(adminPrizeAddDTO.getEndDate());

        // 이미지 파일 저장
        MultipartFile image = adminPrizeAddDTO.getImage();
        if (image != null && !image.isEmpty()) {
            String filePath = fileStore.storePrize(image);
            prize.setImagePath(filePath);

            String imageName = filePath.substring(filePath.lastIndexOf('/') + 1);
            prize.setImageName(imageName);
        }

        prizeRepository.save(prize);
    }

    public Page<AdminPrizeListDTO> getCurrentPrizes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prizeRepository.findCurrentPrizes(pageable);
    }

    public Page<AdminPrizeListDTO> getExpiredPrizes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prizeRepository.findExpiredPrizes(pageable);
    }

    public PrizeDrawDTO getPrizeDraw(Long id) {
        return prizeRepository.findPrizeDrawById(id);
    }

    public Page<AnnouncementListDTO> getAnnouncementPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return announcementRepository.findAnnouncementPage(pageable);
    }

    public void addAnnouncement(AnnouncementWriteDTO announcementWriteDTO) {
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementWriteDTO.getTitle());
        announcement.setContent(announcementWriteDTO.getContent());
        announcementRepository.save(announcement);
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    public void addAnimationsWithGenres(List<Animation> animations, List<List<String>> genreNamesList) {
        for (int i = 0; i < animations.size(); i++) {
            Animation animation = animations.get(i);
            List<String> genreNames = genreNamesList.get(i);

            animationRepository.save(animation); // 애니메이션 저장

            // 1. 장르 이름으로 장르들을 조회합니다.
            List<Genre> genres = genreRepository.findByNameIn(genreNames);

            // 2. 각 애니메이션과 장르를 매핑하는 AnimationGenre 생성 및 저장
            for (Genre genre : genres) {
                AnimationGenre animationGenre = new AnimationGenre(animation, genre);
                animation.getAnimationGenres().add(animationGenre);
            }

            animationRepository.save(animation); // 변경된 애니메이션 저장 (매핑 정보 포함)
        }
    }
}