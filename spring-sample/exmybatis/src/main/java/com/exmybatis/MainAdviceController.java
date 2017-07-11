package com.exmybatis;

import com.exmybatis.domain.RestError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;


@ControllerAdvice
public class MainAdviceController implements ResponseBodyAdvice<Object> {

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

        /*
        logger.info("beforeBodyWrite() URL : {}", request.getURI().toString());

        if (RestException.class.isAssignableFrom(body.getClass())) {
            // 필요한 경우, 이곳에 HTTP 요청 처리 후 응답을 가공하는 로직 작성
            response.getHeaders().add("some-header", "some-value");
            RestException restException = (RestException)body;
            restException.setMessage(restException.getMessage().toUpperCase());
            return restException;
        }
        */
        return body;
    }


    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseBody
    public RestError illegalArgumentException(RuntimeException exception, WebRequest request) {
        RestError err = new RestError();

        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        err.setMessage(exception.getMessage());

        return err;
    }


    @ExceptionHandler(value = {IOException.class})
    public ModelAndView handleIOException(final Exception exception, final HttpServletRequest request) {

        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        ModelAndView mv = new ModelAndView();
        mv.setView(jsonView);
        mv.addObject(new RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception.getMessage()));
        return mv;

        //return new ModelAndView(jsonView, "err", new RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception.getMessage()));

    }


}
