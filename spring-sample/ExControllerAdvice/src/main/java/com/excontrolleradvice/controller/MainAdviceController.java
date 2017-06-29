package com.excontrolleradvice.controller;

import com.excontrolleradvice.exception.BarException;
import com.excontrolleradvice.exception.RestException;
import com.excontrolleradvice.exception.FooException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class MainAdviceController implements ResponseBodyAdvice<Object> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports( MethodParameter returnType,
                             Class<? extends HttpMessageConverter<?>> converterType ) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response ) {

        logger.info("beforeBodyWrite() URL : {}", request.getURI().toString());

        if (RestException.class.isAssignableFrom(body.getClass())) {
            // 필요한 경우, 이곳에 HTTP 요청 처리 후 응답을 가공하는 로직 작성
            response.getHeaders().add("some-header", "some-value");
            RestException restException = (RestException)body;
            restException.setMessage(restException.getMessage().toUpperCase());
            return restException;
        }

        return body;
    }

    @ExceptionHandler(value = {FooException.class})
    @ResponseBody
    public RestException handleFooException(RuntimeException runtimeException, WebRequest request) {
        logger.info("handleFooException");

        RestException restException = new RestException();
        restException.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        restException.setMessage(runtimeException.getMessage());

        return restException;
    }

    @ExceptionHandler(value = {BarException.class})
    @ResponseBody
    public RestException handleBarException(RuntimeException runtimeException, WebRequest request) {
        logger.info("handleBarException");

        RestException restException = new RestException();
        restException.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        restException.setMessage(runtimeException.getMessage());

        return restException;
    }

    @ExceptionHandler
    @ResponseBody
    public RestException handleEtcException(RuntimeException runtimeException,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        logger.info("handleEtcException");
        String contentType = request.getHeader("Content-Type");

        if (null != contentType && MediaType.APPLICATION_JSON.equals(contentType)) {
            logger.info("JSON 형태로 예외처리");
        } else {
            logger.info("그 외 형태로 예외처리");
        }

        RestException restException = new RestException();
        restException.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        restException.setMessage(runtimeException.getMessage());
        return restException;
    }
}
