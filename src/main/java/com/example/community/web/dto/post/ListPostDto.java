package com.example.community.web.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class ListPostDto {
    private Long postId;
    private String title;
    private String content;
    private List<String> images = new ArrayList<>(); //post images
    private int replyCount;
    private int likeCount;
    private int viewCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime writeTime;


    private Long memberId;
    private String userName; //member name
    private String userImage; //member image

    private List<String> badges = new ArrayList<>();
    private Set<String> tags;

    @QueryProjection
    public ListPostDto(Long postId, String title, String content, int replyCount, int likeCount, int viewCount, LocalDateTime writeTime, Long memberId, String userName, String userImage) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.writeTime = writeTime;
        this.memberId = memberId;
        this.userName = userName;
        this.userImage = userImage;

    }


}
