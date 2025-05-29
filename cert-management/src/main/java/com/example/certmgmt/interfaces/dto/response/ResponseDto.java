package com.example.certmgmt.interfaces.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一API响应格式
 * 
 * @param <T> 响应数据类型
 * @author System
 * @since 1.0.0
 */
@Schema(description = "统一响应格式")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    @Schema(description = "状态码，200表示成功", example = "200")
    private int code;

    @Schema(description = "响应消息", example = "成功")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "时间戳", example = "1620000000000")
    private long timestamp;

    public ResponseDto() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(200, "成功", data);
    }

    /**
     * 成功响应（无数据）
     * 
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success() {
        return new ResponseDto<>(200, "成功", null);
    }

    /**
     * 成功响应（自定义消息）
     * 
     * @param message 响应消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(200, message, data);
    }

    /**
     * 失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> failure(int code, String message) {
        return new ResponseDto<>(code, message, null);
    }

    /**
     * 失败响应（默认400错误码）
     * 
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> failure(String message) {
        return new ResponseDto<>(400, message, null);
    }
    
    /**
     * 错误响应（默认400错误码）
     * 
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(400, message, null);
    }
    


    // Getter and Setter methods
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}