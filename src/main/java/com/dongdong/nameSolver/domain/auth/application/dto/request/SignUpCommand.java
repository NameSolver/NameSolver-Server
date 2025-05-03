package com.dongdong.nameSolver.domain.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpCommand {
    private String name;
    private String email;
    private String solvedacName;
    private String id;
    private String password;
}
