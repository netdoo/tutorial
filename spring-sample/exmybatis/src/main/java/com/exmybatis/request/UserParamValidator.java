package com.exmybatis.request;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserParamValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return (UserParamValidator.class.isAssignableFrom(clazz));
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserParam userParam = (UserParam)target;
        if (userParam.userId == null) {
            errors.rejectValue("user_id", String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "잘못된 요청입니다..");
        }
    }
}
