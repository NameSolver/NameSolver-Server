package com.dongdong.nameSolver.domain.member.application.dto.response;

import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {
    private String solvedacName;
    private String name;
    private String email;

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getSolvedacName(), member.getName(), member.getEmail());
    }
}
