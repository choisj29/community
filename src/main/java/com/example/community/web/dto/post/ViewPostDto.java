package com.example.community.web.dto.post;

import com.example.community.web.dto.reply.ReplyDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
public class ViewPostDto {
    private Long postId;
    private String title;
    private String content;
    private String image; // image src
    private int replyCount;
    private int likeCount;
    private int viewCount;
    private Page<ReplyDto> replies;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime writeTime;

    private Long memberId;
    private String userName;
    private String userImage;

    private Set<String> tags;


    @QueryProjection
    public ViewPostDto(Long postId, String title, String content, String image, int replyCount, int likeCount, int viewCount, LocalDateTime writeTime, Long memberId, String userName, String userImage) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.writeTime = writeTime;
        this.memberId = memberId;
        this.userName = userName;
        this.userImage = userImage;
    }


}
