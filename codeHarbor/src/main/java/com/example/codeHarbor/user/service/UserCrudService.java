package com.example.codeHarbor.user.service;

import com.example.codeHarbor.tool.javamail.JavaMailService;
import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.dto.UserCrudRequestDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserCrudService {
    private final UserRepository userRepo;
    private final JavaMailService mailSender;
    public UserCrudResponseDto checkId(UserCrudRequestDto input) {
        String id = input.getUserId();
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        if (!isEmail(id)) {
            response.setSuccess(false);
            data.put("msg", "올바른 아이디 형태가 아닙니다.");
            response.setData(data);
            return response;
        }
        try {
            if (userRepo.existsByUserId(id)) {
                response.setSuccess(false);
                data.put("msg", "이미 가입된 이메일입니다.");
                response.setData(data);
            } else {
                mailSender.sendVerificationMail(id);
                response.setSuccess(true);
                data.put("msg", "인증 메일을 발송했습니다.");
                response.setData(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "인증 메일 발송 과정에서 문제가 발생했습니다.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto verifyCode(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            response = mailSender.confirmVerificationMail(input.getUserId(), input.getVerifyCode());
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "인증 코드 검증 과정에서 문제가 발생했습니다.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto checkExistNick(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            if (userRepo.existsByUserNickname(input.getUserNickname())) {
                response.setSuccess(false);
                data.put("msg", "이미 존재하는 닉네임입니다.");
                response.setData(data);
            } else {
                response.setSuccess(true);
                data.put("msg", "가입 가능한 닉네임입니다.");
                response.setData(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "닉네임 유효성 검증 중 문제가 발생했습니다.");
            response.setData(data);
        }
        return response;
    }
    public UserCrudResponseDto basicSignin(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            UserDomain newUser = new UserDomain();
            newUser.setUserId(input.getUserId());
            newUser.setUserNickname(input.getUserNickname());
            newUser.setUserPassword(input.getUserPassword());
            userRepo.save(newUser);

            response.setSuccess(true);
            data.put("msg", "회원 가입에 성공했습니다.");
            response.setData(data);
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "회원가입 중 문제가 발생했습니다. 관리자에게 문의하세요.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto findPassword(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        if(!isEmail(input.getUserId())) {
            response.setSuccess(false);
            data.put("msg", "올바른 아이디 형태가 아닙니다.");
            response.setData(data);
            return response;
        }
        try {
            UserDomain lostPw = userRepo.findUserByUserId(input.getUserId());
            if (lostPw != null) {
                response = mailSender.sendLostPasswordMail(lostPw.getUserId(), lostPw.getUserPassword());
            } else {
                response.setSuccess(false);
                data.put("msg", "존재하지 않는 이메일입니다.");
                response.setData(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "비밀번호 찾기 도중 문제가 발생했습니다. 문제가 지속되면 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }
    public boolean isEmail(String input) {
        final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
