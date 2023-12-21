package com.example.community.web.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class ModifyMemberDto {
    private Long memberId;
    private String name;
    private MultipartFile image;
    @NotEmpty
    private String password;
    private String newPassword;
}
