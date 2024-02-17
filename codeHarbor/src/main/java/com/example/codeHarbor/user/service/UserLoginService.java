package com.example.codeHarbor.user.service;

import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.dto.UserLoginRequestDto;
import com.example.codeHarbor.user.dto.UserLoginResponseDto;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepo;

    public UserLoginResponseDto basicLogin(UserLoginRequestDto input) {
        UserLoginResponseDto response = new UserLoginResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            UserDomain dbUser = userRepo.findUserByUserId(input.getUserId());
            if (dbUser != null) {
                UserDomain checkUser = userRepo.findUserByUserIdAndUserPassword(input.getUserId(), input.getUserPassword());
                if (dbUser.equals(checkUser)) {
                    response.setSuccess(true);
                    data.put("userId", checkUser.getUserId());
                    data.put("userNickname", checkUser.getUserNickname());
                    response.setData(data);
                } else {
                    response.setSuccess(false);
                    data.put("msg", "아이디 혹은 비밀번호가 일치하지 않습니다.");
                    response.setData(data);
                }
            } else {
                response.setSuccess(false);
                data.put("msg", "아이디 혹은 비밀번호가 일치하지 않습니다.");
                response.setData(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "로그인 과정에서 문제가 발생했습니다. 문제 지속시 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }

}
