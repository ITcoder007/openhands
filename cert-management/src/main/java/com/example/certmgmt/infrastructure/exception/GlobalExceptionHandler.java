package com.example.certmgmt.infrastructure.exception;

import com.example.certmgmt.interfaces.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理参数校验异常 - @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn("参数校验失败: {}", e.getMessage());
        
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error("参数校验失败: " + errorMessage));
    }
    
    /**
     * 处理参数校验异常 - @ModelAttribute
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDto<Void>> handleBindException(BindException e) {
        logger.warn("参数绑定失败: {}", e.getMessage());
        
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error("参数绑定失败: " + errorMessage));
    }
    
    /**
     * 处理参数校验异常 - @RequestParam/@PathVariable
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        logger.warn("参数约束违反: {}", e.getMessage());
        
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error("参数约束违反: " + errorMessage));
    }
    
    /**
     * 处理参数类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.warn("参数类型转换失败: {}", e.getMessage());
        
        String errorMessage = String.format("参数 '%s' 的值 '%s' 无法转换为 %s 类型", 
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(errorMessage));
    }
    
    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("业务逻辑异常: {}", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(e.getMessage()));
    }
    
    /**
     * 处理业务状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDto<Void>> handleIllegalStateException(IllegalStateException e) {
        logger.warn("业务状态异常: {}", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ResponseDto.error(e.getMessage()));
    }
    
    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto<Void>> handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常: ", e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.error("系统内部错误，请稍后重试"));
    }
    
    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleException(Exception e) {
        logger.error("未知异常: ", e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.error("系统内部错误，请稍后重试"));
    }
}