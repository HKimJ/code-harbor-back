package com.example.codeHarbor.tool.javamail;

import com.example.codeHarbor.tool.redis.RedisService;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class JavaMailService {
    private final JavaMailSender mailSender;
    private final RedisService redisService;
    private final int EXPIRE_MIN = 5;

    public UserCrudResponseDto sendVerificationMail(String email, UserCrudResponseDto response) {
        Map<String, Object> data = new HashMap<>();
        String verifyCode = generateCode();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String title = "[CodeHarbor 인증 코드]";
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
            response.setSuccess(true);
            data.put("msg", "인증메일 발송에 성공했습니다.");
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "인증메일 발송 과정에서 문제가 발생했습니다. 문제 지속시 관리자에게 문의해주세요.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto confirmVerificationMail(String email, String verifyCode) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            String storedCode = redisService.retrieveDataFromRedis(email);
            if (storedCode.equals(verifyCode)) {
                response.setSuccess(true);
                data.put("msg", "인증 코드가 일치합니다.");
                response.setData(data);
            } else {
                response.setSuccess(false);
                data.put("msg", "인증 코드가 일치하지 않습니다.");
                response.setData(data);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "인증 코드가 만료되었거나 인증 과정에서 문제가 발생했습니다.");
            response.setData(data);
            return response;
        }
    }

    public UserCrudResponseDto sendLostPasswordMail(String email, String password) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String title = "[CodeHarbor 비밀번호]";
        StringBuilder content = new StringBuilder();
        content.append("<h3> 기존 비밀번호는 다음과 같습니다.</h3>")
                .append("<h1> [ ").append(password).append(" ]</h1>");
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(title); // 메일 제목
            mimeMessageHelper.setText(content.toString(), true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage);
            response.setSuccess(true);
            data.put("msg", "비밀번호 찾기 메일이 발송되었습니다.");
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "비밀번호 찾기 메일 발송에 실패했습니다. 문제 지속시 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }


    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // Generates a 4-digit number between 1000 and 9999
        return String.valueOf(code);
    }
}
