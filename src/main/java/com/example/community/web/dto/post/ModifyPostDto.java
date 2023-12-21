package com.example.community.web.dto.post;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class ModifyPostDto {
    private Long            postId;
    private String          title;      //제목
    private String          content;    //내용
    private String          image;      //이미지 url
    private Long            memberId;
    private String          name;

    @Builder
    public ModifyPostDto(Long postId, String title, String content, Long memberId) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}
