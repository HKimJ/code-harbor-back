package com.example.codeHarbor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class UserCrudResponseDto {
    @Schema(description="요청 성공여부", example = "true")
    @NotNull
    private boolean success;
    @Schema(description = "클라이언트 요청에 따른 유저 정보를 key, value로 유저 데이터 전송<br>" +
            "로그인 실패시 실패 메시지 전달",
            example = "data.userId: test@naver.com " +
                    "data.userNickname: test " +
                    "data.userGroupName : myGroup" +
                    "data.msg: 아이디나 비밀번호가 일치하지 않습니다.")
    private Map<String, Object> data;
}
