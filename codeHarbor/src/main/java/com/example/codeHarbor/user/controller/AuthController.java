package com.example.codeHarbor.user.controller;

import com.example.codeHarbor.user.dto.UserAuthRequestDto;
import com.example.codeHarbor.user.dto.UserAuthResponseDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.service.UserAuthService;
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
@RequestMapping("/auth")
@Tag(name="Auth_Controller", description="요청에 따라 일반 로그인과 SNS 로그인, 유저정보 최신화 등을 수행")
public class AuthController {
    private final UserAuthService authService;
    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com" ,
                    "userPassword" : "testPw"
                } 
            """)}))
    @PostMapping(value = "/basicLogIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "기본 로그인 진행", description = "유저 id, pw를 입력받아 검증하고 로그인 절차 진행")
    public ResponseEntity<UserAuthResponseDto> basicSignIn(@Valid @RequestBody @Parameter(description = "유저가 로그인 시도시 입력한 id, pw") UserAuthRequestDto input, BindingResult bindingResult)
    {
        System.out.println("일반 로그인 시도");
        if (bindingResult.hasErrors()) {
            UserAuthResponseDto response = new UserAuthResponseDto();
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
        UserAuthResponseDto response = authService.basicLogin(input);
        return ResponseEntity.ok(response);
    }



    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com" 
                } 
            """)}))
    @PostMapping(value = "/latestUserInfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원정보 최신화 진행", description = "유저의 상태에 변화를 줄 수 있는 작업 수행 직후 클라이언트 요청시 요청에 포함된 userId에 해당하는 유저를 조회하고 최신화 정보를 전달")
    public ResponseEntity<UserAuthResponseDto> latestUserInfo(@Valid @RequestBody UserAuthRequestDto input, BindingResult bindingResult)
    {
        System.out.println("최신화된 유저정보 요청");
        if (bindingResult.hasErrors()) {
            UserAuthResponseDto response = new UserAuthResponseDto();
            Map<String, Object> data = new HashMap<>();
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
        UserAuthResponseDto response = authService.latestUserInfo(input);
        return ResponseEntity.ok(response);
    }

//    @PostMapping(value = "/sns", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "SNS 로그인 진행", description = "SNS 로그인 진행(추후 분기)")
//    public ResponseEntity<UserAuthResponseDto> snsSignIn(@Valid @RequestBody UserAuthRequestDto input)
//    {
//        System.out.println("SNS 로그인 시도");
//        UserAuthResponseDto response = auuthService.;
//        return ResponseEntity.ok(response);
//    }
}
