package hello.service;

import hello.dto.admin.*;
import hello.entity.animation.Animation;
import hello.entity.request.Request;
import hello.entity.request.RequestStatus;
import hello.repository.AnimationRepository;
import hello.repository.FileStore;
import hello.repository.RequestRepository;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static hello.entity.request.RequestStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AnimationRepository animationRepository;
    private final FileStore fileStore;
    private final RequestRepository requestRepository;

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

        // 이미지 파일 저장
        MultipartFile image = animationDTO.getImage();
        if (image != null && !image.isEmpty()) {
            String filePath = fileStore.storeAnimation(image);
            animation.setImagePath(filePath);
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

    public void rejectRequest(RequestResponseDTO requestResponseDTO) {
        Request request = requestRepository.findById(requestResponseDTO.getId()).get();
        if (request != null) {
            request.setResponse(requestResponseDTO.getResponse());
            request.setStatus(REJECTED);
        }
    }
}