package com.example.codeHarbor.plan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@Builder
public class PlanCrudResponseDto {
    @Schema(description = "계획 crud 요청에 대한 성공여부", example = "true")
    private final boolean success;
    @Schema(description = "계획 crud 요청에 대한 응답 데이터와 메세지", example = "msg : 성공/실패/오류...")
    private final Map<String, Object> data;
}
