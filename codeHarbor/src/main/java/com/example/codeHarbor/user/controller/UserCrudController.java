package com.example.codeHarbor.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userCrud")
@Tag(name="userCrud_Controller", description="요청에 따라 회원가입, 회원정보 수정, 회원탈퇴 등을 수행하는 컨트롤러")
public class UserCrudController {

}
