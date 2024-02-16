package com.example.codeHarbor.user.service;

import com.example.codeHarbor.tool.javamail.JavaMailService;
import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.dto.UserCrudRequestDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCrudService {
    private final UserRepository userRepo;
    private final JavaMailService mailSender;
    public UserCrudResponseDto checkId(UserCrudRequestDto input) {
        String id = input.getUserId();
        UserCrudResponseDto response = new UserCrudResponseDto();
        try {
            if (userRepo.existsByUserId(id)) {
                response.setSuccess(false);
                response.setData("이미 가입된 이메일입니다.");
            } else {
                mailSender.sendVerificationMail(id);
                response.setSuccess(true);
                response.setData("인증 메일을 발송했습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setData("중복 체크시 문제가 발생했습니다. 관리자에게 문의해주세요.");
        }
        return response;
    }

    public UserCrudResponseDto verifyCode(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        try {
            response = mailSender.confirmVerificationMail(input.getUserId(), input.getVerifyCode());

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setData("인증코드 검증과정에서 문제가 발생했습니다.");
        }
        return response;
    }

    public UserCrudResponseDto checkExistNick(UserCrudRequestDto input) {
        String nick = input.getUserNickname();
        UserCrudResponseDto response = new UserCrudResponseDto();
        try {
            if (userRepo.existsByUserNickname(nick)) {
                response.setSuccess(false);
                response.setData("중복된 닉네임입니다.");
            } else {
                response.setSuccess(true);
                response.setData("가입 가능한 닉네임입니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setData("문제가 발생했습니다. 관리자에게 문의해주세요.");
        }
        return response;
    }
    public UserCrudResponseDto basicSignin(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        String id = input.getUserId();
        String nick = input.getUserNickname();
        String pw = input.getUserPassword();
        try {
            UserDomain newUser = new UserDomain();
            newUser.setUserId(id);
            newUser.setUserNickname(nick);
            newUser.setUserPassword(pw);
            userRepo.save(newUser);

            response.setSuccess(true);
            response.setData("회원 가입에 성공했습니다.");
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setData("회원가입 중 문제가 발생했습니다. 관리자에게 문의하세요");

        }
        return response;
    }
}
