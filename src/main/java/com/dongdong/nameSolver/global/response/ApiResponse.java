package com.dongdong.nameSolver.global.response;

import lombok.Getter;

import java.util.Map;

@Getter
public class ApiResponse<T> {
    private int code;
    private T data;
    private String error;

    public ApiResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ApiResponse(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, data);
    }

    public static ApiResponse<Map<String, String>> error(int code, String message) {
        return new ApiResponse<>(code, message);
    }
}