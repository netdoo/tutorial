package com.exjetty;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 1. preHandle - controller 이벤트 호출전
 2. postHandle - controller 호출 후 view 페이지 출력전
 3. afterCompletion - controller + view 페이지 모두 출력 후
 */

public class HttpInterceptor2 implements HandlerInterceptor {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("preHandle2");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info("postHandle2");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("afterCompletion2");
    }

    /*
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        // HTTP 요청 처리 전 수행할 로직 작성

        RequestMapping rm = ((HandlerMethod) handler).getMethodAnnotation(RequestMapping.class);
        boolean alreadyLoggedIn = request.getSession().getAttribute("user") != null;
        boolean loginPageRequested = rm != null && rm.value().length > 0 && "login".equals(rm.value()[0]);

        if (alreadyLoggedIn && loginPageRequested) {
            response.sendRedirect(request.getContextPath() + "/app/main-age");
            return false;
        } else if (!alreadyLoggedIn && !loginPageRequested) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        logger.info("preHandle");
        return true;
    }

    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView ) throws Exception {
        // HTTP 요청 처리 후 수행할 로직 작성
        //response.setB
        logger.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("afterCompletion");
    }
    */
}
