package hello.service.basic;

import hello.dto.user.MyRequestDTO;
import hello.dto.user.RequestDTO;
import hello.entity.request.Request;
import hello.entity.user.User;
import hello.repository.db.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public void writeRequest(RequestDTO requestDTO, User user) {
        Request request = new Request();
        request.setTitle(requestDTO.getTitle());
        request.setContent(requestDTO.getContent());
        request.setUser(user);
        requestRepository.save(request);
    }

    public Page<MyRequestDTO> getMyRequest(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return requestRepository.findMyRequest(user, pageable);
    }

    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}