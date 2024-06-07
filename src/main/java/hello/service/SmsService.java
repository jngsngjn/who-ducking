package hello.service;

import hello.repository.CodeStore;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${smsApiKey}")
    private String apiKey;

    @Value("${smsApiSecret}")
    private String apiSecret;

    @Value("${myPhone}")
    private String senderPhoneNumber;

    private DefaultMessageService messageService;
    private final CodeStore store;

    public SmsService(CodeStore store) {
        this.store = store;
    }

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void sendCode(String phone) {
        Message message = new Message();
        message.setFrom(senderPhoneNumber);
        message.setTo(phone);
        int code = getRandomPassword();
        message.setText("[Who's DucKing]\n인증번호를 정확히 입력해 주세요.\n" + "인증번호 : " + code);

        try {
//            messageService.send(message); // 실행하면 돈 나감..! 가끔 테스트해봐도 되는데 아껴씁시다 ㅎㅎ
            System.out.println(code); // 문자 보내지 않고 로그에서 확인해도 됨
            store.storeCode(phone, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getRandomPassword() {
        return (int) (Math.random() * 9000) + 1000;
    }
}