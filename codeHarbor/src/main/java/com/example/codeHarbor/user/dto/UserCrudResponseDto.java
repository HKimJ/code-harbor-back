package com.example.codeHarbor.user.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserCrudResponseDto {
    private boolean success;
    private Map<String, Object> data;
}
