package com.example.codeHarbor.user.controller;

import com.example.codeHarbor.user.dto.UserLoginRequestDto;
import com.example.codeHarbor.user.dto.UserLoginResponseDto;
import com.example.codeHarbor.user.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/login")
@Tag(name="Login_Controller", description="요청에 따라 일반 로그인과 SNS 로그인을 수행")
public class LogInController {
    private final UserLoginService loginService;
    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com" ,
                    "userPassword" : "testPw"
                } 
            """)}))
    @PostMapping(value = "/basic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "기본 로그인 진행", description = "유저 id, pw를 입력받아 검증하고 로그인 절차 진행")
    public ResponseEntity<UserLoginResponseDto> basicSignIn(@Valid @RequestBody @Parameter(description = "유저가 로그인 시도시 입력한 id, pw") UserLoginRequestDto input, BindingResult bindingResult)
    {
        System.out.println("일반 로그인 시도");
        if (bindingResult.hasErrors()) {
            UserLoginResponseDto response = new UserLoginResponseDto();
            Map<String, Object> data =  new HashMap<>();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            response.setSuccess(false);
            data.put("msg", "올바른 매개변수명이나 형식이 전달되지 않았습니다.");
            response.setData(data);
            return ResponseEntity.ok(response);
        }
        UserLoginResponseDto response = loginService.basicLogin(input);
        return ResponseEntity.ok(response);
    }

//    @PostMapping(value = "/sns", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "SNS 로그인 진행", description = "SNS 로그인 진행(추후 분기)")
//    public ResponseEntity<UserLoginrResponseDto> snsSignIn(@Valid @RequestBody UserLoginRequestDto input)
//    {
//        System.out.println("SNS 로그인 시도");
//        UserLoginrResponseDto response = userLoginService.;
//        return ResponseEntity.ok(response);
//    }
}
