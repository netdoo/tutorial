package com.exaop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectObject {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public AspectObject() {
        logger.info("Create AspectObject");
    }

    /// 어플리케이션에 존재하는 모든 라이브러리에서 doSomething라는 메서드를 캐치함.
    @Around("execution(* doSomething(..))")
    public Object onAroundDoSomethingMethod(final ProceedingJoinPoint joinPoint) {

        Object obj = null;

        try {
            logger.info("onAroundDoSomeMethod {} 실행 전 ", joinPoint.getSignature().getName());
            obj = joinPoint.proceed();
            logger.info("onAroundDoSomeMethod {} 실행 후 ", joinPoint.getSignature().getName());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return obj;
    }

    /// 어플리케이션에 존재하는 모든 라이브러리에서 doSome 이 포함된 메서드를 캐치함.
    @Around("execution(* doSome*(..))")
    public Object onAroundDoSomeMethod(final ProceedingJoinPoint joinPoint) {

        Object obj = null;

        try {
            logger.info("onAroundDoSomeMethod {} 실행 전 ", joinPoint.getSignature().getName());
            obj = joinPoint.proceed();
            logger.info("onAroundDoSomeMethod {} 실행 후 ", joinPoint.getSignature().getName());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return obj;
    }

    /// com.exaop 패키지의 DummyObject 클래스의 printName 메소드만 캐치함.
    @Around("execution(* com.exaop.DummyObject.printName(..))")
    public Object onDummyObjectPrintNameMethod(final ProceedingJoinPoint joinPoint) {

        Object obj = null;

        try {
            logger.info("onDummyObjectPrintNameMethod {} 실행 전 ", joinPoint.getSignature().getName());
            obj = joinPoint.proceed();
            logger.info("onDummyObjectPrintNameMethod {} 실행 후 ", joinPoint.getSignature().getName());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return obj;
    }

    /// com.exaop 패키지의 DummyObject 클래스의 모든 메소드를 캐치함.
    @Around("execution(* com.exaop.DummyObject.*(..))")
    public Object onDummyObjectAllMethod(final ProceedingJoinPoint joinPoint) {

        Object obj = null;

        try {
            logger.info("onDummyObjectAllMethod {} 실행 전 ", joinPoint.getSignature().getName());
            obj = joinPoint.proceed();
            logger.info("onDummyObjectAllMethod {} 실행 후 ", joinPoint.getSignature().getName());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return obj;
    }

    /// com.exaop 패키지의 모든 클래스의 모든 메소드를 캐치함.
    @Around("execution(* com.exaop.*.*(..))")
    public Object onAllClassAllMethod(final ProceedingJoinPoint joinPoint) {

        Object obj = null;

        try {
            logger.info("onAllClassAllMethod {} 실행 전 ", joinPoint.getSignature().getName());
            obj = joinPoint.proceed();
            logger.info("onAllClassAllMethod {} 실행 후 ", joinPoint.getSignature().getName());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return obj;
    }
}
