package hello.service.playgroud;

import hello.dto.playground.prize.PrizeBasicDTO;
import hello.entity.prize.Entry;
import hello.entity.prize.Prize;
import hello.entity.prize.PrizeGrade;
import hello.entity.user.User;
import hello.repository.db.EntryRepository;
import hello.repository.db.PrizeRepository;
import hello.service.account.EmailService;
import hello.service.basic.PointService;
import hello.service.register.SmsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class PrizeService {

    private final int ENTRY_POINT = 30;

    private final PrizeRepository prizeRepository;
    private final EntryRepository entryRepository;
    private final PointService pointService;
    private final SmsService smsService;
    private final EmailService emailService;

    public boolean checkPoint(User user) {
        int userPoint = user.getPoint();
        return userPoint >= ENTRY_POINT;
    }

    public boolean checkPrizeGradeAndLevel(Prize prize, User user) {
        PrizeGrade grade = prize.getGrade();
        Long level = user.getLevel().getId();

        switch (grade) {
            case N:
                return level >= 1;
            case R:
                return level >= 4;
            case SR:
                return level >= 7;
            case UR:
                return level == 10;
            default:
                return false;
        }
    }

    public boolean alreadyEntry(User user) {
        LocalDate lastDrawDate = user.getLastDrawDate();
        LocalDate today = LocalDate.now();

        // lastDrawDate가 null이 아니고, 오늘 날짜와 동일하면 이미 응모한 것으로 간주
        return lastDrawDate != null && lastDrawDate.isEqual(today);
    }

    public boolean isAddressEmpty(User user) {
        String detailedAddress = user.getHomeAddress().getDetailedAddress();
        return detailedAddress == null || detailedAddress.isEmpty();
    }

    // 개선 필요 (추후 예정)
    public void entryPrize(Long prizeId, User user) {
        LocalDate today = LocalDate.now();
        user.setLastDrawDate(today);

        Prize prize = prizeRepository.findById(prizeId).orElseThrow();
        Optional<Entry> findEntry = entryRepository.findByUserIdAndPrizeId(user.getId(), prizeId);
        pointService.decreasePoint(user, 30);

        // 처음 응모한 경우
        if (findEntry.isEmpty()) {
            Entry entry = new Entry(user, prize);
            entryRepository.save(entry);
        } else {
            // 한 번 이상 응모한 경우
            findEntry.get().setEntryCount(findEntry.get().getEntryCount() + 1);
            entryRepository.save(findEntry.get());
        }
    }

    public User randomDraw(Long prizeId) throws MessagingException, URISyntaxException, IOException {
        List<Entry> entries = entryRepository.findByPrizeId(prizeId);
        Prize prize = prizeRepository.findById(prizeId).orElseThrow();
        if (entries.isEmpty()) {
            return null;
        }

        List<User> drawPool = new ArrayList<>();
        for (Entry entry : entries) {
            for (int i = 0; i < entry.getEntryCount(); i++) {
                drawPool.add(entry.getUser());
            }
        }

        Random random = new Random();
        int winnerIndex = random.nextInt(drawPool.size());

        User winner = drawPool.get(winnerIndex);
        prize.setUser(winner);

        smsService.sendToWinner(winner.getPhone(), winner.getNickname(), prize.getName());
        emailService.sendToWinner(winner.getEmail(), winner.getNickname(), prize);
        return winner;
    }

    public PrizeBasicDTO getEarliestPrizeByGrade(PrizeGrade grade) {
        Pageable firstResult = PageRequest.of(0, 1);
        List<PrizeBasicDTO> result = prizeRepository.findFirstByGradeOrderByStartDateAsc(grade, firstResult);
        return result.isEmpty() ? null : result.get(0);
    }

    public Prize findById(Long prizeId) {
        return prizeRepository.findById(prizeId).orElseThrow();
    }

    public List<PrizeBasicDTO> getPrizePage(PrizeGrade grade) {
        return prizeRepository.findPrizePageByGrade(grade);
    }
}