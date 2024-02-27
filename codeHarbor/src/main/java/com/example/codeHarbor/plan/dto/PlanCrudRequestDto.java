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
    @Schema(description = "계획 관련 요청을 보낸 이용자 ID", example = "test@naver.com")
    private final String planOwner;
    @Schema(description = "계획생성 요청을 보낸 이용자가 소속된 groupName", example = "testGroup")
    private final String GroupName;
    @Schema(description = "처리를 원하는 계획의 내용", example = "finished my plan")
    private final String PlanContent;
    @Schema(description = "이용자가 계획을 완료했는지 여부를 체크하는 필드", example = "true/false")
    private final boolean isDone;

}
