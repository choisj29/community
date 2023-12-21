package com.example.community.domain.reply;
import com.example.community.domain.member.Member;
import com.example.community.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Reply {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    private String          content;    //내용
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime   writeTime;  //작성시간
    private boolean isDeleted = false;  //삭제여부
    private String name;  //작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setPost(Post post) {
        this.post = post;
        post.getReplies().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getReplies().add(this);
    }

    public void modify(String content) {
        this.content = content;
    }

    public Reply(String content, LocalDateTime writeTime, Post post, Member member, String name) {
        this.content = content;
        this.writeTime = writeTime;
        this.name = name;
        setPost(post);
        setMember(member);
    }
}
