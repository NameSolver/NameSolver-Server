package com.dongdong.nameSolver.domain.member.presentation;

import com.dongdong.nameSolver.domain.member.application.dto.response.MemberResponse;
import com.dongdong.nameSolver.domain.member.application.service.MemberService;
import com.dongdong.nameSolver.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    public ApiResponse<MemberResponse> getMember(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(memberService.findMember(memberId));
    }

    @DeleteMapping("")
    public ApiResponse<Boolean> signOut(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(memberService.signOut(memberId));
    }
}
