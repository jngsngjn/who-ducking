package hello.service;

import hello.dto.admin.*;
import hello.entity.animation.Animation;
import hello.entity.genre.AnimationGenre;
import hello.entity.genre.Genre;
import hello.entity.prize.Prize;
import hello.entity.request.Request;
import hello.entity.request.RequestStatus;
import hello.repository.*;
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

    public void addPrize(PrizeAddDTO prizeAddDTO) throws IOException {
        Prize prize = new Prize();

        prize.setName(prizeAddDTO.getName());
        prize.setGrade(prizeAddDTO.getGrade());
        prize.setEndDateTime(prizeAddDTO.getEndDateTime());

        // 이미지 파일 저장
        MultipartFile image = prizeAddDTO.getImage();
        if (image != null && !image.isEmpty()) {
            String filePath = fileStore.storePrize(image);
            prize.setImagePath(filePath);

            String imageName = filePath.substring(filePath.lastIndexOf('/') + 1);
            prize.setImageName(imageName);
        }

        prizeRepository.save(prize);
    }

    public Page<PrizeListDTO> getCurrentPrizes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prizeRepository.findCurrentPrizes(pageable);
    }

    public Page<PrizeListDTO> getExpiredPrizes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prizeRepository.findExpiredPrizes(pageable);
    }
}