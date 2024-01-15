package com.example.community.web.dto.member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class WithdrawalDto {
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력하세요.")
    private String password;

    @Builder
    public WithdrawalDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
