package com.example.codeHarbor.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCrudRequestDto {
    @NotNull
    @Email
    private String userId;
    @NotNull
    private String userPw;
}
