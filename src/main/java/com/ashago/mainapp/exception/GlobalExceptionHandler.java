package com.ashago.mainapp.exception;

import com.ashago.mainapp.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SessionNotValidException.class)
    public CommonResp handleSessionNotValidException(HttpServletRequest httpServletRequest, final SessionNotValidException e, HttpServletResponse httpServletResponse){
        return CommonResp.create("801", "Please login first.");
    }

    @ExceptionHandler(Exception.class)
    public CommonResp handleCommonException(HttpServletRequest httpServletRequest, final Exception e, HttpServletResponse httpServletResponse) {
        log.error("some err.", e);
        httpServletResponse.setStatus(500);
        return CommonResp.create("501", "Unknown Err.");
    }
}
