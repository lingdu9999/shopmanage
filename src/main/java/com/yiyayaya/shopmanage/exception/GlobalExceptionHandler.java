package com.yiyayaya.shopmanage.exception;

import com.yiyayaya.shopmanage.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        log.error("业务异常: {}", e.getMessage()); // 只记录异常消息
        return Result.error(500, e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public Result<?> handleValidationException(ValidationException e) {
        log.error("参数校验异常: {}", e.getMessage());
        return Result.error(400, e.getMessage()); // 返回 400 状态码
    }

    @ExceptionHandler(AuthException.class)
    public Result<?> handleAuthException(AuthException e) {
        log.error("权限异常: {}", e.getMessage());
        return Result.error(403, e.getMessage()); // 返回 403 状态码
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常", e); // 记录完整的堆栈信息
        return Result.error(500, "系统异常，请稍后重试");
    }
}
