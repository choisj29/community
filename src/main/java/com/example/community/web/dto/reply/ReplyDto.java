package com.example.community.web.dto.reply;

import com.example.community.domain.reply.Reply;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReplyDto {

    private Long replyId;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime writeTime;
    private String name;     //member name
    private boolean isDeleted; //댓글삭제여부

    private Long postId;
    private Long memberId;

    //댓글 페이징
    private Long replyCnt; //총 댓글개수
    private int replyPage; //댓글 페이지

    public ReplyDto(Reply reply) {
        this.replyId = reply.getId();
        this.content = reply.isDeleted() ? null : reply.getContent();
        this.writeTime = reply.getWriteTime();
        this.name = reply.getMember().getName();
        this.isDeleted = reply.isDeleted();
        this.postId = reply.getPost().getId();
        this.memberId = reply.getMember().getId();
    }

    @QueryProjection
    public ReplyDto(Long replyId, String content, LocalDateTime writeTime, String name, boolean isDeleted) {
        this.replyId = replyId;
        this.content = content;
        this.writeTime = writeTime;
        this.name = name;
        this.isDeleted = isDeleted;
    }
}
