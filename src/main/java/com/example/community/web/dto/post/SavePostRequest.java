package com.example.community.web.dto.post;

import com.example.community.domain.member.Member;
import com.example.community.domain.post.Post;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class SavePostRequest {
    @NotEmpty
    @ApiParam(value = "제목", required = true)
    private String title;

    @ApiParam(value = "내용", required = true)
    private String content;

    @ApiParam(value = "뷰 횟수", required = true)
    private int viewCount;

    @ApiParam(value = "닉네임", required = true)
    private String name;

    private Long memberId;

    @ApiParam(value = "이미지", required = true)
    private List<MultipartFile> images = new ArrayList<>();

    @Builder
    public SavePostRequest(String title, String content, String name, Long memberId) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.memberId = memberId;
    }

    public Post toEntity(){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }

    public void setMemberInfo(Member member) {
        this.name = member.getName();
        this.memberId = member.getId();
    }
}
