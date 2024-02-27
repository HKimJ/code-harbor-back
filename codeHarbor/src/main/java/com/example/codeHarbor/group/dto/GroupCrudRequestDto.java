package com.example.codeHarbor.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@Builder
public class GroupCrudRequestDto {
    @Schema(description = "최초 그룹생성 요청을 보낸 이용자 id", example = "test@naver.com")
    private final String groupCreator;
    @Schema(description = "그룹생성 요청시 전송한 그룹이름", example = "myGroup1")
    private final String groupName;
    @Schema(description = "그룹생성시 그룹이름 중복 체크했는지 여부", example = "true/false")
    private final boolean isChecked;
    @Schema(description = "그룹원 초대 요청을 보낸 이용자 id", example = "invitor@naver.com")
    private final String groupInvitor;
    @Schema(description = "그룹에 초대할 인원의 userId or email 주소)", example = "invitee@naver.com")
    private final String groupInvitee;
    @Schema(description = "그룹가입 신청한 인원의 userId or email 주소)", example = "Joiner@naver.com")
    private final String groupJoiner;
    @Schema(description = "그룹에 초대할 인원의 인증용 value(추후 토큰등으로 교체)", example = "certainValue")
    private final String groupInviteVerify;
    @Schema(description = "단순 로그 저장용으로 전송하는 userId", example = "someone@naver.com")
    private final String userId;

}
