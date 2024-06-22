package hello.service.basic;

import hello.entity.user.User;
import hello.repository.db.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;

    public void increasePoint(User user, int point) {
        System.out.println("increase point : " + point);
        int result = user.getPoint() + point;
        user.setPoint(result);
        userRepository.save(user);
    }

    public void decreasePoint(User user, int point) {
        int currentPoint = user.getPoint();
        if (currentPoint >= point) {
            user.setPoint(currentPoint - point);
            userRepository.save(user);
        }
    }
}