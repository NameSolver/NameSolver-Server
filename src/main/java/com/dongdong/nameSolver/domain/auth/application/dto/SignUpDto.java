package com.dongdong.nameSolver.domain.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpDto {
    private String name;
    private String email;
    private String solvedacName;
    private String id;
    private String password;


}
