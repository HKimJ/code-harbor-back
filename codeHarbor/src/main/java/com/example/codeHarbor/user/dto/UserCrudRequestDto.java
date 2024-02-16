package com.example.codeHarbor.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class UserCrudRequestDto {
    @Email
    @Nullable
    private String userId;
    @Nullable
    private String userNick;
    @Nullable
    private String userPw;
    @Nullable
    private String verifyCode;
}
