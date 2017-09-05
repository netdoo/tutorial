package com.expagenotfound.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class MainControllerAdvice {
    /**
     * Throwable Exception
     */
    @ExceptionHandler(Throwable.class)
    public void handleServerError(HttpServletRequest req, Exception ex) {
        //todo

    }


    /**
     * 404 not found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView NoHandlerFoundException(HttpServletRequest req, Exception ex) {
        //todo
        ModelAndView mav = new ModelAndView("page_not_found");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
}
