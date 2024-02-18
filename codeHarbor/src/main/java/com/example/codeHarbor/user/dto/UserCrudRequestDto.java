package com.example.codeHarbor.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

@Getter
@ToString
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class UserCrudRequestDto {
    @Email @Nullable
    @Schema(name="userId", description = "클라이언트에서 입력한 메일 형태의 이용자 ID", example = "test@naver.com")
    private String userId;
    @Nullable
    @Schema(description = "클라이언트에서 입력한 이용자 닉네임", example = "tester1")
    private String userNickname;
    @Nullable
    @Schema(description = "클라이언트에서 입력한 이용자 비밀번호", example = "testPw")
    private String userPassword;
    @Nullable
    @Schema(description = "클라이언트에서 입력한 이메일 인증코드", example = "1234")
    private String verifyCode;


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
