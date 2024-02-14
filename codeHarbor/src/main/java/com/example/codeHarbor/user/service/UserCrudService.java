package com.example.codeHarbor.user.service;

import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.dto.UserCrudRequestDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.dto.UserLoginRequestDto;
import com.example.codeHarbor.user.dto.UserLoginrResponseDto;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCrudService {
    private final UserRepository userRepo;

    public UserCrudResponseDto createUser(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        String id = input.getUserId();
        String pw = input.getUserPw();
//        try {
//            UserDomain isNewUser = userRepo.findUserByUserId(id);
//            if (isNewUser != null) {
//                UserDomain checkUser = userRepo.findUserByUserIdAndUserPassword(id, pw);
//                if (dbUser.equals(checkUser)) {
//                    response.setSuccess(true);
//
//                } else {
//
//                }
//            } else {
//                response.setSuccess(false);
//                response.setData("아이디 혹은 비밀번호가 일치하지 않습니다.");
//            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
