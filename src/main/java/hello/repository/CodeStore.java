package hello.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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