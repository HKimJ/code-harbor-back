package com.example.codeHarbor.user.controller;

import com.example.codeHarbor.user.dto.UserCrudRequestDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.service.UserCrudService;
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
@RequestMapping("/userCrud")
@Tag(name="userCrud_Controller", description="요청에 따라 회원가입, SNS 회원가입, 회원정보 수정, 회원 탈퇴 등을 수행")
public class UserCrudController {

    private final UserCrudService crudService;
    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com" 
                } 
            """)}))
    @PostMapping(value = "/checkId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "일반 회원가입 시 아이디(이메일) 유효성 및 중복 검증", description = "유저 id 입력받아 중복 및 형식 유효성을 검증하고 성공시 인증메일 발송 및 요청 수행 결과 데이터 전송")
    public ResponseEntity<UserCrudResponseDto> checkId(@Valid @RequestBody UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("이메일 유효성 검증 시도");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
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
        UserCrudResponseDto response = crudService.checkId(input);
        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com",
                    "verifyCode" : "1234" 
                } 
            """)}))
    @PostMapping(value = "/verifyCode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "인증코드 일치여부 판별", description = "유저가 입력한 인증코드와 메일로 발송된 인증코드 일치여부 판별")
    public ResponseEntity<UserCrudResponseDto> checkVerifyCode(@Valid @org.springframework.web.bind.annotation.RequestBody @Parameter(description = "사용자가 입력한 아이디와 인증코드", example = "userid: test@test.com, verifyCode: 1234") UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("사용자 아이디와 인증코드 일치여부 판별");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
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
        UserCrudResponseDto response = crudService.verifyCode(input);
        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userNickname" : "tester1" 
                } 
            """)}))
    @PostMapping(value = "/checkNickname", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "일반 회원가입 시 닉네임 유효성 및 중복 검증", description = "유저 닉네임 입력받아 중복 및 형식 유효성을 검증하고 결과에 따른 데이터 전송")
    public ResponseEntity<UserCrudResponseDto> checkNickname(@Valid @RequestBody UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("닉네임 유효성 검증 시도");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
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
        UserCrudResponseDto response = crudService.checkExistNick(input);
        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com",
                    "userNickname" : "tester1",
                    "userPassword" : "testPw"
                } 
            """)}))
    @PostMapping(value = "/signupBasic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "일반 회원가입 진행", description = "이메일 및 닉네임 유효성 검증이 끝난 상태에서 회원가입 요청시 회원가입 처리")
    public ResponseEntity<UserCrudResponseDto> basicSignIn(@Valid @RequestBody UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("회원가입 요청");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
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
        UserCrudResponseDto response = crudService.basicSignin(input);
        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com" 
                } 
            """)}))
    @PostMapping(value = "/findPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "비밀번호 찾기", description = "요청시 입력한 이메일로 기존 비밀번호를 전송")
    public ResponseEntity<UserCrudResponseDto> findPassword(@Valid @RequestBody UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("비밀번호 찾기 요청");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
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
        UserCrudResponseDto response = crudService.findPassword(input);
        return ResponseEntity.ok(response);
    }
    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com" 
                } 
            """)}))
    @PostMapping(value = "/refreshUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원정보 최신화 진행", description = "클라이언트 요청시 요청에 포함된 userId에 해당하는 유저의 최신화 정보를 전달")
    public ResponseEntity<UserCrudResponseDto> refreshUser(@Valid @RequestBody UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("최신화된 유저정보 요청");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
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
        UserCrudResponseDto response = crudService.refreshUser(input);
        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody (content = @Content(
            examples = {
                    @ExampleObject(name = "Example", value = """ 
                { 
                    "userId" : "test@example@test.com" ,
                    "userGroupname" : "newGroupName" / null,
                    "userNickname" : "newNickname" / null,
                    "userPassword" : "newPassword" / null,
                    etc...
                } 
            """)}))
    @PostMapping(value = "/modifyUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원정보 최신화 진행", description = "클라이언트 요청시 요청에 포함된 유저정보 해당하는 유저의 정보로 db를 업데이트")
    public ResponseEntity<UserCrudResponseDto> modifyUser(@Valid @RequestBody UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("유저정보 변경 요청");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
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
        UserCrudResponseDto response = crudService.modifyUserInfo(input);
        return ResponseEntity.ok(response);
    }


//    @PostMapping(value = "/signupSNS", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "SNS 회원가입 진행", description = "SNS 고유값을 받아 회원가입 처리")
//    public ResponseEntity<UserCrudResponseDto> snsSignIn(@Valid @RequestBody @Parameter(description = "SNS 로그인 요청시 받는 SNS 고유값", example = "snsId") UserCrudRequestDto input, BindingResult bindingResult)
//    {
//        System.out.println("SNS 회원가입 시도");
//        if (bindingResult.hasErrors()) {
//            UserCrudResponseDto response = new UserCrudResponseDto();
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors) {
//                String fieldName = error.getField();
//                String errorMsg = error.getDefaultMessage();
//                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
//            }
//            response.setSuccess(false);
//            response.setData("아이디 혹은 비밀번호가 일치하지 않습니다.");
//            return ResponseEntity.ok(response);
//        }
//        UserCrudResponseDto response = crudService.basicUserCrud(input);
//        return ResponseEntity.ok(response);
//    }

}
