package com.example.codeHarbor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

@Getter
@ToString
@AllArgsConstructor
public class UserCrudRequestDto {
    @Email @Nullable
    @Schema(description = "클라이언트에서 입력하거나 현재 이용자의 메일 형태의 이용자 ID", example = "test@naver.com")
    private final String userId;
    @Nullable
    @Schema(description = "클라이언트에서 입력하거나 현재 이용자의 닉네임", example = "tester1")
    private final String userNickname;
    @Nullable
    @Schema(description = "클라이언트에서 입력하거나 현재 이용자의 닉네임", example = "myGroupName")
    private final String userGroupName;
    @Nullable
    @Schema(description = "클라이언트에서 입력한 이용자 비밀번호", example = "testPw")
    private final String userPassword;
    @Nullable
    @Schema(description = "클라이언트에서 입력한 이메일 인증코드", example = "1234")
    private final String verifyCode;


//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//    public void setUserNickname(String userNickname) {
//        this.userNickname = userNickname;
//    }
//    public void setUserPassword(String userPassword) {
//        this.userPassword = userPassword;
//    }
//    public void setVerifyCode(String verifyCode) {
//        this.verifyCode = verifyCode;
//    }



}
