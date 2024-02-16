package com.example.codeHarbor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {
    @NotNull
    @Email
    @Schema(description="로그인시 사용자가 입력한 아이디", example = "test@naver.com")
    private String userId;
    @Schema(description="로그인시 사용자가 입력한 비밀번호", example = "test1234")
    private String userPassword; // SNS 로그인에서 불필요
}
