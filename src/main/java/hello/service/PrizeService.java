package hello.service;

import hello.entity.user.User;
import hello.repository.PrizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PrizeService {

    private final int ENTRY_POINT = 30;

    private final PrizeRepository prizeRepository;

    public boolean checkPoint(User user) {
        int userPoint = user.getPoint();
        return userPoint >= ENTRY_POINT;
    }

    // 경품 등급과 레벨 확인

    public void entryPrize(Long prizeId, User user) {

    }
}