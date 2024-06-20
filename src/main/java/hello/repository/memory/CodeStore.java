package hello.repository.memory;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// 전화번호 인증코드를 저장하는 메모리 저장소
@Component
public class CodeStore {

    private final Map<String, Integer> store = new HashMap<>();

    public void storeCode(String phone, int code) {
        store.put(phone, code);
    }

    public boolean verifyCode(String phone, int inputCode) {
        if (store.containsKey(phone)) {
            int storedCode = store.get(phone);
            return storedCode == inputCode;
        }
        return false;
    }

    public void removeCode(String phone) {
        store.remove(phone);
    }
}