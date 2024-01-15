package com.example.community.web.dto.member;

import com.example.community.domain.member.Member;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long memberId;

    private String loginId;

    private String name;

    public MemberDto(Member member) {
        this.memberId = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
    }
}
