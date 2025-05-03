package com.dongdong.nameSolver.domain.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ReissueCommand {
    private String refreshToken;
    private UUID memberId;
}
