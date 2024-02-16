package com.example.codeHarbor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class UserCrudRequestDto {
    @Email @Nullable
    @Schema(description = "클라이언트에서 입력한 메일 형태의 이용자 ID", example = "test@naver.com")
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
}
