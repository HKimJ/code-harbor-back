//package com.example.codeHarbor.tool.requestBodyAdviser;
//
//import com.example.codeHarbor.user.controller.UserCrudController;
//import com.example.codeHarbor.user.dto.UserCrudRequestDto;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
//import java.lang.reflect.Type;
//
//
//@RestControllerAdvice(basePackageClasses = UserCrudController.class)
//public class UserCrudRequestBodyControllerAdviser implements RequestBodyAdvice {
//
//    @Override
//    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        System.out.println("supports 호출");
//        return methodParameter.getContainingClass() == UserCrudController.class && targetType.getTypeName().equals(UserCrudRequestDto.class.getTypeName());
//    }
//
//    @Override
//    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        System.out.println("beforeBodyRead 호출");
//        System.out.println(inputMessage.getHeaders());
//        return inputMessage;
//    }
//
//    @Override
//    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        System.out.println("afterBodyRead 호출");
//        System.out.println(parameter.getParameter());
//        System.out.println(body.toString());
//        String requestUrl = getRequestUrl(parameter);
//        UserCrudRequestDto requestDto;
//        switch (requestUrl) {
//            case "/verifyCode" :
//                requestDto = UserCrudRequestDto.builder()
//                        .userId(((UserCrudRequestDto) body).getUserId())
//                        .verifyCode(((UserCrudRequestDto) body).getVerifyCode()).build();
//                return requestDto;
//            case "/checkNickname" :
//               requestDto = UserCrudRequestDto.builder().
//                        userNickname(((UserCrudRequestDto) body).getUserNickname()).build();
//                return requestDto;
//            case "/signupBasic" :
//                requestDto = UserCrudRequestDto.builder().
//                        userId(((UserCrudRequestDto) body).getUserId()).
//                        userPassword(((UserCrudRequestDto) body).getUserPassword()).
//                        userNickname(((UserCrudRequestDto) body).getUserNickname()).build();
//                return requestDto;
//            default:
//                requestDto = UserCrudRequestDto.builder().userId(((UserCrudRequestDto) body).getUserId()).build();
//                return requestDto;
//        }
//    }
//
//    @Override
//    public Object handleEmptyBody(final Object body, final HttpInputMessage inputMessage, final MethodParameter parameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
//        if (body == null) {
//            throw new IllegalArgumentException("requestBody가 비어있음");
//        }
//        return body;
//    }
//
//    private String getRequestUrl(MethodParameter parameter) {
//        // 요청 핸들러 메서드의 URL을 얻어옵니다.
//        // 예: @GetMapping("/url1")인 경우 "/url1"을 반환합니다.
//        return parameter.getMethodAnnotation(RequestMapping.class).value()[0];
//    }
//}
