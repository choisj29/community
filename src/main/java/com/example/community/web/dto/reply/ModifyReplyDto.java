package com.example.community.web.dto.reply;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ModifyReplyDto {
    private String content;

    public ModifyReplyDto(String content) {
        this.content = content;
    }
}