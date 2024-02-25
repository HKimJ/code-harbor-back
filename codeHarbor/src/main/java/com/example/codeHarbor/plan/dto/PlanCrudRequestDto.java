package com.example.codeHarbor.plan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Getter
@ToString
@AllArgsConstructor
public class PlanCrudRequestDto {
    @Email
    @Schema(description = "계획생성 요청을 보낸 이용자 ID", example = "test@naver.com")
    private final String userId;
    @Schema(description = "계획생성 요청을 보낸 이용자 groupName", example = "testGroup")
    private final String GroupName;
    @Nullable
    @Schema(description = "이용자가 입력한 계획 내용", example = "finish my plan")
    private final String PlanContent;

}
