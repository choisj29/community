package com.example.community.web.dto.reply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyChildrenDto {
    private Long   parentId;
    private Long   replyId;
    private String content;
    private int likeCount;
    private String name;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime writeTime;

    private String image;

    @QueryProjection
    public ReplyChildrenDto(Long parentId, Long replyId, String content, int likeCount, String name, String userName, LocalDateTime writeTime, String image) {
        this.parentId = parentId;
        this.replyId = replyId;
        this.content = content;
        this.likeCount = likeCount;
        this.name = name;
        this.userName = userName;
        this.writeTime = writeTime;
        this.image = image;
    }

}
