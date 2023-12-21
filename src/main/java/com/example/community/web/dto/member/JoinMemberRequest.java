package com.example.community.web.dto.member;

import com.example.community.domain.member.Member;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class JoinMemberRequest {
    @NotEmpty(message = "아이디를 입력하세요.")
    @ApiParam(value = "로그인 아이디", required = true)
    private String loginId;    //아이디

    @NotEmpty(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @ApiParam(value = "비밀번호", required = true)
    private String password;   //비밀번호

    @NotEmpty(message = "닉네임을 입력하세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    @ApiParam(value = "닉네임", required = true)
    private String name;       //닉네임

    @ApiParam(value = "이미지", required = true)
    private MultipartFile image;

    @Builder
    public JoinMemberRequest(String loginId, String password, String name, MultipartFile image) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.image = image;
    }

    public Member toEntity(String encodedPassword){
        return Member.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .name(this.name)
                .build();
    }


}
