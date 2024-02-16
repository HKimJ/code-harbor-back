package com.example.codeHarbor.tool.javamail;

import com.example.codeHarbor.tool.redis.RedisService;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class JavaMailService {
    private final JavaMailSender mailSender;
    private final RedisService redisService;
    private final int EXPIRE_MIN = 3;

    public void sendVerificationMail(String email) {
        String verifyCode = generateCode();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String title = "CodeHarbor 인증 코드";
        StringBuilder content = new StringBuilder();

        content.append("<h3> 요청하신 인증 코드는 다음과 같습니다.</h3>")
                .append("<h1> [ ").append(verifyCode).append(" ]</h1>")
                .append("<h3> 인증 코드는 ").append(EXPIRE_MIN).append("분 후에 만료됩니다.</h3>");

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(title); // 메일 제목
            mimeMessageHelper.setText(content.toString(), true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage);
            redisService.storeDataInRedis(email, verifyCode, EXPIRE_MIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserCrudResponseDto confirmVerificationMail(String email, String verifyCode) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        try {
            String storedCode = redisService.retrieveDataFromRedis(email);
            if (storedCode.equals(verifyCode)) {
                response.setSuccess(true);
                response.setData("인증 성공");
            } else {
                response.setSuccess(false);
                response.setData("인증코드가 일치하지 않습니다.");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setData("인증 코드가 만료되었거나 인증 과정에서 문제가 발생했습니다.");
            return response;
        }
    }
    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // Generates a 4-digit number between 1000 and 9999
        return String.valueOf(code);
    }
}
