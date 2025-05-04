package com.dongdong.nameSolver.domain.member.presentation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/member")
public class MemberController {
    @GetMapping("")
    public void getMember(@AuthenticationPrincipal UUID memberId) {

    }

    @DeleteMapping("")
    public void signout(@AuthenticationPrincipal UUID memberId) {
        
    }
}
