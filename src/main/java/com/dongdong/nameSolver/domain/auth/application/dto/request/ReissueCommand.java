package com.dongdong.nameSolver.domain.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReissueCommand {
    private String accessToken;
    private String refreshToken;
    private String memberId;
}
