package com.dongdong.nameSolver.domain.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthKeyResponse {
    private String key;
}
