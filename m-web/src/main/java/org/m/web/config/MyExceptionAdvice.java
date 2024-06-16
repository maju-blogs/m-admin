package org.m.web.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionAdvice {

    /**
     * 自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = NotLoginException.class)
    public SaResult bizExceptionHandler(NotLoginException ex, HttpServletResponse response) {
        log.error("自定义业务异常： ", ex);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return SaResult.error(ex.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public SaResult bizExceptionHandler(RuntimeException ex, HttpServletResponse response) {
        log.error("系统异常", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return SaResult.error(ex.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    public SaResult bizExceptionHandler(Exception ex, HttpServletResponse response) {
        log.error("系统异常", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return SaResult.error("内部错误");
    }
}