package com.example.codeHarbor.tool.javamail;

import com.example.codeHarbor.group.dto.GroupCrudResponseDto;
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
    public GroupCrudResponseDto sendGroupInvitaiondMail(String email, String groupName, String redirectUrl) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String title = "[CodeHarbor 그룹 초대]";
        StringBuilder content = new StringBuilder();
        content.append("<h1> [ ").append(groupName).append(" ] 그룹에서 사용자를 초대하려고 합니다.</h1>")
                .append("<h2> 하단의 링크를 통해 CodeHarbor로 이동하고 그룹가입 요청을 수락해주세요. </h2>")
                .append("<a href=\"").append(redirectUrl).append("\"/>");
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(title); // 메일 제목
            mimeMessageHelper.setText(content.toString(), true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage);
            data.put("msg", "그룹 초대메일이 발송되었습니다.");
            response = GroupCrudResponseDto.builder().success(true).data(data).build();
        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "그룹 초대메일 발송에 실패했습니다. 문제 지속시 관리자에게 연락해주세요.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }

//  수락 메일을 보내는 로직(cors 등 해결법 찾으면 채택 고려)
//    public GroupCrudResponseDto sendGroupInvitaiondMail(String email, String groupName, String acceptUrl, String verifyValue) {
//        GroupCrudResponseDto response;
//        Map<String, Object> data = new HashMap<>();
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        String title = "[CodeHarbor 그룹 초대]";
//        StringBuilder content = new StringBuilder();
//        content.append("<h1> [ ").append(groupName).append(" ] 그룹에서 사용자를 초대하려고 합니다.</h1>")
//                .append("<h2> 하단의 링크를 눌러 초대를 수락해주세요. </h2>")
//                .append("<form action=\"").append(acceptUrl).append("\" method=\"post\">")
//                .append("<input type=\"hidden\" name=\"groupInvitee\" value=\"").append(email).append("\">")
//                .append("<input type=\"hidden\" name=\"groupName\" value=\"").append(groupName).append("\">")
//                .append("<input type=\"hidden\" name=\"groupInviteVerify\" value=\"").append(verifyValue).append("\">")
//                .append("<button type=\"submit\">Accept</button>")
//                .append("</form>");
//        try {
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//            mimeMessageHelper.setTo(email); // 메일 수신자
//            mimeMessageHelper.setSubject(title); // 메일 제목
//            mimeMessageHelper.setText(content.toString(), true); // 메일 본문 내용, HTML 여부
//            mailSender.send(mimeMessage);
//            // 이 부분은 레디스가 유일한 key값을 가져야하므로 key값을 파싱할 수 있는 구분자를 붙여서 넣는방식으로 api간 키값 중복을 방지해야함 -> 추후 리펙터링
//            redisService.storeDataInRedis(email, verifyValue, EXPIRE_MIN);
//            data.put("msg", "그룹 초대메일이 발송되었습니다.");
//            response = GroupCrudResponseDto.builder().success(true).data(data).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            data.put("msg", "그룹 초대메일 발송에 실패했습니다. 문제 지속시 관리자에게 연락해주세요.");
//            response = GroupCrudResponseDto.builder().success(false).data(data).build();
//        }
//        return response;
//    }

    public GroupCrudResponseDto sendSignUpMail(String email, String groupName, String redirectUrl) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String title = "[CodeHarbor 가입 안내]";
        StringBuilder content = new StringBuilder();
        content.append("<h1> [ ").append(groupName).append(" ] 그룹에서 사용자를 초대하려고 합니다.</h1>")
                .append("<h2> 서비스를 이용하시려면 하단 링크를 클릭해 CodeHarbor에 가입해주세요. </h2>")
                .append("<form action=").append(redirectUrl).append(" method=\"POST\">")
                .append("<input type=\"hidden\" name=\"groupName\" value=\"").append(groupName).append("\">")
                .append("<input type=\"hidden\" name=\"groupInvitee\" value=\"").append(email).append("\">")
                .append("<button type=\"submit\">Accept</button>")
                .append("</form>");
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(title); // 메일 제목
            mimeMessageHelper.setText(content.toString(), true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage);
            data.put("msg", "가입링크 메일이 발송되었습니다.");
            response = GroupCrudResponseDto.builder().success(true).data(data).build();
        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "가입링크 메일 발송에 실패했습니다. 문제 지속시 관리자에게 연락해주세요.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }
    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // Generates a 4-digit number between 1000 and 9999
        return String.valueOf(code);
    }
}
