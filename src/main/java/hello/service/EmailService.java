package hello.service;

import hello.entity.user.EmailCode;
import hello.repository.EmailCodeRepository;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final EmailCodeRepository emailCodeRepository;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final int ALPHABET_COUNT = 3;
    private static final int DIGIT_COUNT = 3;

    public void sendVerificationCode(String to) throws MessagingException, IOException, URISyntaxException {
        String verificationCode = generateVerificationCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Path path = Paths.get(getClass().getClassLoader().getResource("/Logo/whoduck.jpg").toURI());
        byte[] imageBytes = Files.readAllBytes(path);
        DataSource dataSource = new ByteArrayDataSource(imageBytes, "image/jpeg");

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        String html = templateEngine.process("email/verificationEmail", context);

        helper.setTo(to);
        helper.setSubject("[Who's Ducking] 계정 삭제 인증 코드");
        helper.setText(html, true);
        helper.addInline("logoImage", dataSource);

        saveCode(to, verificationCode);
        javaMailSender.send(message);
        System.out.println("toEmail = " + to);
        System.out.println("verificationCode = " + verificationCode);
    }

    private void saveCode(String to, String verificationCode) {
        boolean existsByEmail = emailCodeRepository.existsByEmail(to);
        if (existsByEmail) {
            emailCodeRepository.deleteByEmail(to);
        }
        emailCodeRepository.save(new EmailCode(to, verificationCode));
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(ALPHABET_COUNT + DIGIT_COUNT);

        for (int i = 0; i < ALPHABET_COUNT; i++) {
            int index = random.nextInt(UPPERCASE_CHARACTERS.length());
            code.append(UPPERCASE_CHARACTERS.charAt(index));
        }

        for (int i = 0; i < DIGIT_COUNT; i++) {
            int index = random.nextInt(DIGITS.length());
            code.append(DIGITS.charAt(index));
        }

        List<Character> codeChars = new ArrayList<>();
        for (char c : code.toString().toCharArray()) {
            codeChars.add(c);
        }
        Collections.shuffle(codeChars);

        StringBuilder shuffledCode = new StringBuilder();
        for (char c : codeChars) {
            shuffledCode.append(c);
        }

        return shuffledCode.toString();
    }

    public EmailCode findByEmail(String email) {
        return emailCodeRepository.findByEmail(email);
    }

    // 매 1분마다 실행
    @Scheduled(fixedRate = 60000)
    public void deleteExpiredCodes() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime expireTime = now.minusMinutes(5);
        emailCodeRepository.deleteExpiredCodes(expireTime);
    }

    public boolean verifyCode(String email, String code) {
        EmailCode findEmailCode = findByEmail(email);
        return findEmailCode != null
                && findEmailCode.getCode().equalsIgnoreCase(code)
                && findEmailCode.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5));
    }
}