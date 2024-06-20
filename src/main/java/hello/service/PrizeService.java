package hello.service;

import hello.entity.prize.Entry;
import hello.entity.prize.Prize;
import hello.entity.prize.PrizeGrade;
import hello.entity.user.User;
import hello.repository.EntryRepository;
import hello.repository.PrizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class PrizeService {

    private final int ENTRY_POINT = 30;

    private final PrizeRepository prizeRepository;
    private final EntryRepository entryRepository;

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

    public void entryPrize(Long prizeId, User user) {
        LocalDate today = LocalDate.now();
        user.setLastDrawDate(today);

        Prize prize = prizeRepository.findById(prizeId).orElseThrow();
        Entry findEntry = entryRepository.findByUserId(user.getId()).orElseThrow();

        // 처음 응모한 경우
        if (findEntry == null) {
            Entry entry = new Entry(user, prize);
            entryRepository.save(entry);
        } else {
            // 한 번 이상 응모한 경우
            findEntry.setEntryCount(findEntry.getEntryCount() + 1);
        }
    }

    public User randomDraw(Long prizeId) {
        List<Entry> entries = entryRepository.findByPrizeId(prizeId);
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
        return drawPool.get(winnerIndex);
    }
}