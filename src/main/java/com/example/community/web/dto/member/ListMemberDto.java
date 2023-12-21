package com.example.community.web.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListMemberDto {
    private Long memberId;
    private String loginId;
    private String name;
    private String image;

    @QueryProjection
    public ListMemberDto(Long memberId, String loginId, String name) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.name = name;
    }
}
