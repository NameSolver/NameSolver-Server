package com.dongdong.nameSolver.global.response;

import java.util.Map;

public class ApiResponse<T> {
    private int code;
    private T data;

    public ApiResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, data);
    }

    public static ApiResponse<Map<String, String>> error(int code, String message) {
        return new ApiResponse<>(code, Map.of("error", message));
    }
}