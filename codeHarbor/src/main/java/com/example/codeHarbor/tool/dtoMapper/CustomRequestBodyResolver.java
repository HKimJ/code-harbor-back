package com.example.codeHarbor.tool.dtoMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.service.invoker.RequestBodyArgumentResolver;

@Component
public class CustomRequestBodyResolver extends RequestBodyArgumentResolver {

    private final ObjectMapper objectMapper;

    public CustomRequestBodyResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        try {
            // 요청 본문을 문자열로 변환
            String requestBody = inputMessage.getBody().toString();

            // 요청 본문에서 필요한 필드만을 추출하여 해당 필드만을 가진 DTO 객체를 생성
            // 여기서는 간단히 ObjectMapper를 사용하여 요청 본문을 해당 DTO 클래스로 매핑하고, 필요한 필드만을 추출하는 것으로 가정
            UserRequest userRequest = objectMapper.readValue(requestBody, UserRequest.class);

            // 필요한 필드만을 가진 DTO 객체 반환
            return userRequest;
        } catch (IOException e) {
            throw new HttpMessageNotReadableException("Error reading request body", e);
        }
    }
}
