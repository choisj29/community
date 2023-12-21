package com.example.community.domain.memberPostLike;

import com.example.community.domain.member.Member;
import com.example.community.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class MemberPostLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_post_like_id")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime likeTime; //좋아요 누른 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //연관관계 메서드
    public void setMember(Member member){
        this.member = member;
        member.getMemberPostLikes().add(this);
    }

    public void setPost(Post post){
        this.post = post;
        post.getMemberPostLikes().add(this);
    }

    //생성 메서드
    public static MemberPostLike createMemberPostLike(Member member, Post post) {
        MemberPostLike memberPostLike = new MemberPostLike();
        memberPostLike.setPost(post);
        memberPostLike.setMember(member);
        memberPostLike.setLikeTime(LocalDateTime.now());
        return memberPostLike;
    }
}
