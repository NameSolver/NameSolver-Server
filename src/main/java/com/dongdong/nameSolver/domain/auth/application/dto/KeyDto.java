package com.dongdong.nameSolver.domain.auth.application.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class KeyDto {
    private UUID key;

    @Builder
    public KeyDto(UUID key) {
        this.key = key;
    }
}
