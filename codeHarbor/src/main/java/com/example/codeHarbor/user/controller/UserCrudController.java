package com.example.codeHarbor.user.controller;

import com.example.codeHarbor.user.dto.UserCrudRequestDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.service.UserCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userCrud")
@Tag(name="userCrud_Controller", description="요청에 따라 회원가입, 회원정보 수정, 회원 탈퇴 등을 수행")
public class UserCrudController {

    private final UserCrudService crudService;

    @PostMapping(value = "/checkId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "일반 회원가입 시 아이디(이메일) 유효성 및 중복 검증", description = "유저 id 입력받아 중복 및 유효성을 검증하고 결과에 따른 데이터 전송")
    public ResponseEntity<UserCrudResponseDto> checkId(@Valid @RequestBody @Parameter(description = "일반 로그인 요청시 받는 id, pw", example = "userId") UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("이메일 유효성 검증 시도");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            response.setSuccess(false);
            response.setData("올바른 이메일 형태가 아닙니다.");
            return ResponseEntity.ok(response);
        }
        UserCrudResponseDto response = crudService.checkId(input);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/verifyCode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "인증코드 일치여부 판별", description = "유저가 입력한 인증코드와 메일로 발송된 인증코드 일치여부 판별")
    public ResponseEntity<UserCrudResponseDto> checkVerifyCode(@Valid @RequestBody @Parameter(description = "사용자가 입력한 인증코드", example = "1234") UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("인증코드 일치여부 판별");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            response.setSuccess(false);
            response.setData("올바른 인증코드 형태가 아닙니다.");
            return ResponseEntity.ok(response);
        }
        UserCrudResponseDto response = crudService.verifyCode(input);
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/signupBasic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "일반 회원가입 진행", description = "유저 id, pw를 입력받아 이메일 유효성을 검증하고 통과시 회원가입 처리")
    public ResponseEntity<UserCrudResponseDto> basicSignIn(@Valid @RequestBody @Parameter(description = "일반 로그인 요청시 받는 id, pw", example = "userId") UserCrudRequestDto input, BindingResult bindingResult)
    {
        System.out.println("회원가입 시도");
        if (bindingResult.hasErrors()) {
            UserCrudResponseDto response = new UserCrudResponseDto();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMsg = error.getDefaultMessage();
                System.out.println("입력 유효성 검증 실패 - 필드명: " + fieldName + ", 에러메세지: " + errorMsg);
            }
            response.setSuccess(false);
            response.setData("아이디 혹은 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.ok(response);
        }
        UserCrudResponseDto response = crudService.basicUserCrud(input);
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
