package com.exjetty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class HttpResponseAdvice implements ResponseBodyAdvice<Object> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports( MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType ) {
        return true;
    }

    @Override
    public Object beforeBodyWrite( Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response ) {
        // HTTP 요청 처리 후 응답을 가공하는 로직 작성
        response.getHeaders().add("some-header", "some-value");
        logger.info("beforeBodyWrite");
        return body.toString().toUpperCase();
        //return body;
    }
}

