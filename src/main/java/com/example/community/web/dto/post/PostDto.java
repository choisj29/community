package com.example.community.web.dto.post;

import com.example.community.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long            postId;
    private String          title;      //제목
    private String          content;    //내용
    private String          image;      //이미지 url
    private LocalDateTime   writeTime;  //작성시간
    private int             viewCount;  //뷰 횟수

    public PostDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();
        this.writeTime = post.getCreatedTime();
        this.viewCount = post.getViewCount();
    }
}
