package com.example.codeHarbor.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Getter
@ToString
@Builder
public class GroupCrudResponseDto {
    @Schema(description = "그룹 crud 요청에 대한 성공여부", example = "true")
    private final boolean success;
    @Schema(description = "그룹 crud 요청에 대한 응답 메세지", example = "msg : 성공/실패/오류...")
    private final Map<String, Object> data;
}
