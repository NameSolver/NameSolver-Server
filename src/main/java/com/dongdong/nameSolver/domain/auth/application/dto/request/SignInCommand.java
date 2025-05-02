package com.dongdong.nameSolver.domain.auth.application.dto.request;

import lombok.Data;

@Data
public class SignInCommand {
    private String id;
    private String password;
}
