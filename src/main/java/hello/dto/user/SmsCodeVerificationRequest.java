package hello.dto.user;

import lombok.Data;

@Data
public class SmsCodeVerificationRequest {

    private String phone;
    private int code;
}