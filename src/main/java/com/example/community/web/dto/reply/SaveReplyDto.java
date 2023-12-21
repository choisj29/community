package com.example.community.web.dto.reply;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveReplyDto {
    private Long memberId;
    private Long postId;
    private String content;

    public SaveReplyDto(Long memberId, Long postId, String content) {
        this.memberId = memberId;
        this.postId = postId;
        this.content = content;
    }
}
