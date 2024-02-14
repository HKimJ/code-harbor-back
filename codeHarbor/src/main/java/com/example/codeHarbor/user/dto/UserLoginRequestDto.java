package com.example.codeHarbor.user.dto;

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
    private String userId;

    private String userPassword; // SNS 로그인에서 불필요
}
