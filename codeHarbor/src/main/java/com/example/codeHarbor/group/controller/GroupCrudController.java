package com.example.codeHarbor.group.controller;

import com.example.codeHarbor.group.dto.GroupCrudRequestDto;
import com.example.codeHarbor.group.dto.GroupCrudResponseDto;
import com.example.codeHarbor.group.service.GroupCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/groupCrud")
@Tag(name="groupCrud_Controller", description="요청에 따라 그룹생성, 그룹원 초대, 그룹정보 수정, 그룹삭제 등을 수행")
public class GroupCrudController {

    private final GroupCrudService crudService;

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "groupName" : "myGroup" 
                } 
            """)}))
    @PostMapping(value = "/checkGroupName", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "그룹 생성자가 입력한 정보를 바탕으로 그룹생성", description = "그룹명 전달받아 중복체크")
    public ResponseEntity<GroupCrudResponseDto> checkGroupName(@Valid @RequestBody GroupCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("그룹 생성시 그룹이름 중복체크 시도");
        if (bindingResult.hasErrors()) {
            Map<String, Object> data = new HashMap<>();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            data.put("msg", "올바른 매개변수명이나 형식이 전달되지 않았습니다.");
            GroupCrudResponseDto response = GroupCrudResponseDto.builder().success(false).data(data).build();
            return ResponseEntity.ok(response);
        }
        GroupCrudResponseDto response = crudService.checkGroupName(input);
        return ResponseEntity.ok(response);
    }
    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "groupCreator" : "test@example@test.com",
                    "groupName" : "myGroup" 
                    "isChecked" : true / false
                } 
            """)}))
    @PostMapping(value = "/createGroup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "그룹 생성자가 입력한 정보를 바탕으로 그룹생성", description = "그룹명과 그룹생성자를 전달받아 그룹생성")
    public ResponseEntity<GroupCrudResponseDto> createGroup(@Valid @RequestBody GroupCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("그룹 생성 시도");
        if (bindingResult.hasErrors()) {
            Map<String, Object> data = new HashMap<>();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            data.put("msg", "올바른 매개변수명이나 형식이 전달되지 않았습니다.");
            GroupCrudResponseDto response = GroupCrudResponseDto.builder().success(false).data(data).build();
            return ResponseEntity.ok(response);
        }
        GroupCrudResponseDto response = crudService.createGroup(input);
        return ResponseEntity.ok(response);
    }
    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "groupName" : "myGroup",
                    "groupInvitor" : "oldMember@naver.com",
                    "groupInvitee" : "newMember@naver.com" 
                } 
            """)}))
    @PostMapping(value = "/inviteGroup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "그룹원이 새로운 인원을 초대하는 요청", description = "초대자의 그룹명과 초대할 인원의 이메일(userId)을 전달받아 그룹에 초대")
    public ResponseEntity<GroupCrudResponseDto> inviteGroup(@Valid @RequestBody GroupCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("그룹원 초대 시도");
        if (bindingResult.hasErrors()) {
            Map<String, Object> data = new HashMap<>();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            data.put("msg", "올바른 매개변수명이나 형식이 전달되지 않았습니다.");
            GroupCrudResponseDto response = GroupCrudResponseDto.builder().success(false).data(data).build();
            return ResponseEntity.ok(response);
        }
        GroupCrudResponseDto response = crudService.inviteMember(input);
        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                {
                    "groupName" : invitingGroup,
                    "groupInvitee" : "newMember@naver.com"
                }
            """)}))
    @CrossOrigin
    @PostMapping(value = "/acceptExistMember")
    @Operation(summary = "그룹원이 기존에 서비스에 가입된 인원을 그룹에 초대하는 api", description = "초대자의 그룹명과 초대할 인원의 이메일(userId)을 전달받아 그룹에 초대")
    public ResponseEntity<GroupCrudResponseDto> acceptExistUserAsMember(@Valid @RequestBody GroupCrudRequestDto input) {
        System.out.println("그룹원 초대 수락버튼 클릭됨");
        GroupCrudResponseDto response = crudService.acceptExistUserAsMember(input);
        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "verify" : "certainValue"(메일 전송시 임의의 값으로 설정되어있음, 프론트는 상관안해도 됨)
                } 
            """)}))
    @PostMapping(value = "/acceptNewMember")
    @Operation(summary = "그룹원이 새로 서비스에 가입된 인원을 그룹에 초대하는 요청", description = "초대자의 그룹명과 초대할 인원의 이메일(userId)을 전달받아 그룹에 초대")
    public ResponseEntity<GroupCrudResponseDto> acceptNewUserAsMember(@RequestParam String groupInvitee, @RequestParam String verify) {
        System.out.println("그룹원 초대 수락버튼 클릭됨");
        GroupCrudResponseDto response = crudService.acceptExistUserAsMember(GroupCrudRequestDto.builder().groupInvitee(groupInvitee).groupInviteVerify(verify).build());
        return ResponseEntity.ok(response);
    }
}
