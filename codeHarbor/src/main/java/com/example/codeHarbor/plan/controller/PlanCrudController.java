package com.example.codeHarbor.plan.controller;

import com.example.codeHarbor.plan.dto.PlanCrudRequestDto;
import com.example.codeHarbor.plan.dto.PlanCrudResponseDto;
import com.example.codeHarbor.plan.service.PlanCrudService;
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
@RequestMapping("/planCrud")
@Tag(name="planCrud_Controller", description="요청에 따라 계획 생성, 계획 조회, 계획 수정, 계획 삭제 등을 수행")
public class PlanCrudController {
    private final PlanCrudService crudService;

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                {   "planCreator" : "test@naver.com",
                    "groupName" : "myGroup",
                    "planContent" : "do example_plan until 2pm"
                } 
            """)}))
    @PostMapping(value = "/createPlan", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이용자가 입력한 정보를 바탕으로 계획 생성", description = "계획내용 전달받아 추가")
    public ResponseEntity<PlanCrudResponseDto> checkGroupName(@Valid @RequestBody PlanCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("계획생성 시도");
        if (bindingResult.hasErrors()) {
            Map<String, Object> data = new HashMap<>();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            data.put("msg", "올바른 매개변수명이나 형식이 전달되지 않았습니다.");
            PlanCrudResponseDto response = PlanCrudResponseDto.builder().success(false).data(data).build();
            return ResponseEntity.ok(response);
        }
        PlanCrudResponseDto response = crudService.createPlan(input);
        return ResponseEntity.ok(response);
    }
}
