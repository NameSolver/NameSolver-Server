package com.dongdong.nameSolver.domain.match.presentation;

import com.dongdong.nameSolver.domain.match.application.dto.request.AcceptMatchCommand;
import com.dongdong.nameSolver.domain.match.application.dto.request.CreateMatchCommand;
import com.dongdong.nameSolver.domain.match.application.dto.response.MatchResponse;
import com.dongdong.nameSolver.domain.match.application.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {
    private final MatchService matchService;

    @PostMapping()
    public MatchResponse createMatch(@AuthenticationPrincipal UUID memberId, @RequestBody CreateMatchCommand createMatchCommand) {
        return matchService.createMatch(memberId, createMatchCommand);
    }

    @PutMapping("/request")
    public void acceptMatch(@AuthenticationPrincipal UUID memberId, @RequestBody AcceptMatchCommand acceptMatchCommand) {
        matchService.acceptMatch(memberId, acceptMatchCommand);
    }
}
