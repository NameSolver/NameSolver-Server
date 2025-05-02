package com.dongdong.nameSolver.domain.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreateAuthKeyCommand {
    private String solvedacName;
}
