package com.example.codeHarbor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {

    @Schema(title="요청 성공여부")
    @NotNull
    private boolean success;

    @Schema(title="메세지", description = "요청 성공여부에 대한 간략한 메세지 전송")
    @NotNull
    private String data;

    @Schema(title="유저정보", description ="요청이 성공했을 시 유저정보를 Key(String), Value(Object)로 전송(추후 토큰 도입후 삭제 예정)")
    private Map<String, Object> userData;
}
