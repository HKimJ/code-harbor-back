package com.example.codeHarbor.user.service;

import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.dto.UserLoginRequestDto;
import com.example.codeHarbor.user.dto.UserLoginResponseDto;
import com.example.codeHarbor.user.dto.UserLoginrResponseDto;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepo;

    public UserLoginResponseDto basicLogin(UserLoginRequestDto input) {
        UserLoginResponseDto response = new UserLoginResponseDto();
        String id = input.getUserId();
        String pw = input.getUserPassword();
        try {
            UserDomain dbUser = userRepo.findUserByUserId(id);
            if (dbUser != null) {
                UserDomain checkUser = userRepo.findUserByUserIdAndUserPassword(id, pw);
                if (dbUser.equals(checkUser)) {
                    response.setSuccess(true);

                } else {

                }
            } else {
                response.setSuccess(false);
                response.setData("아이디 혹은 비밀번호가 일치하지 않습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setData("로그인 과정에서 문제가 발생했습니다. 관리자에게 문의해주세요.");
        }

        return response;
    }
}
