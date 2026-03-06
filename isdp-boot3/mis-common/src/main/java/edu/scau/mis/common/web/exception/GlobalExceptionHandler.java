package edu.scau.mis.common.web.exception;

import edu.scau.mis.common.web.domain.ApiResult;
import edu.scau.mis.common.web.domain.HttpCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResult<String>> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址 '{}',权限不足'{}'", requestURI, e.getMessage());
        return ResponseEntity.status(HttpCode.FORBIDDEN.getCode()).body(ApiResult.fail(HttpCode.FORBIDDEN));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResult<String>> handleMethodArgumentTypeMismatchException(NoResourceFoundException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}',请求资源不存在:'{}'", requestURI, e.getMessage());
        return ResponseEntity.status(HttpCode.NOT_FOUND.getCode()).body(ApiResult.fail(HttpCode.NOT_FOUND));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResult<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',请求参数不合法'{}'",requestURI,e.getMessage());
        return ResponseEntity.status(HttpCode.BAD_REQUEST.getCode()).body(ApiResult.error(HttpCode.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<String>> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}',发生内部错误:'{}'", requestURI, e.getMessage());
        return ResponseEntity.status(HttpCode.INTERNAL_SERVER_ERROR.getCode()).body(ApiResult.error(HttpCode.INTERNAL_SERVER_ERROR));
    }
}
