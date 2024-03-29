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
public class UserAuthResponseDto {

    @Schema(description="요청 성공여부", example = "true")
    @NotNull
    private boolean success;

    @Schema(description = "Auth 요청 성공시 key, value로 유저 데이터 전송<br>" +
            "로그인 실패시 실패 메시지 전달",
            example = "data.userId: test@naver.com " +
                    "data.userNickname: test " +
                    "data.userGroup : {0,null}, {1, groupName}, {2, groupName}"
                    )
    @NotNull
    private Map<String, Object> data;

}
