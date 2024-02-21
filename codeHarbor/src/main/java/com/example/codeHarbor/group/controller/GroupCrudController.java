package com.example.codeHarbor.group.controller;

import com.example.codeHarbor.group.dto.GroupCrudRequestDto;
import com.example.codeHarbor.group.dto.GroupCrudResponseDto;
import com.example.codeHarbor.group.service.GroupCrudService;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    "groupCreator" : "test@example@test.com",
                    "groupName" : "myGroup" 
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
}
