package hello.service.basic;

import hello.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    public void increasePoint(User user, int point) {
        user.setPoint(user.getPoint() + point);
    }

    public void decreasePoint(User user, int point) {
        int currentPoint = user.getPoint();
        if (currentPoint >= point) {
            user.setPoint(currentPoint - point);
        }
    }
}