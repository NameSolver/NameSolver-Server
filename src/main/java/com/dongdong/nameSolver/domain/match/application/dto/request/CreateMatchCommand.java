package com.dongdong.nameSolver.domain.match.application.dto.request;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMatchCommand {
    private MatchType matchType;
}
