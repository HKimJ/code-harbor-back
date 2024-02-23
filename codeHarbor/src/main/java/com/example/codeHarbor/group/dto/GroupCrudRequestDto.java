package com.example.codeHarbor.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@Builder

public class GroupCrudRequestDto {
    @Schema(description = "그룹생성 요청을 보낸 이용자 id", example = "test@naver.com")
    private final String groupCreator;
    @Schema(description = "그룹생성 요청시 전송한 그룹이름", example = "myGroup1")
    private final String groupName;
    @Schema(description = "그룹에 초대할 인원의 userId(email)", example = "invitee@naver.com")
    private final String groupInvitee;
    @Schema(description = "그룹에 초대할 인원의 인증용 value(추후 토큰등으로 교체)", example = "certainValue")
    private final String groupInviteVerify;
}
