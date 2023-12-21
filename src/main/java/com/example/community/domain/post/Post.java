package com.example.community.domain.post;


import com.example.community.domain.member.Member;
import com.example.community.domain.memberPostLike.MemberPostLike;
import com.example.community.domain.reply.Reply;
import com.example.community.global.entity.BaseTimeEntity;
import com.example.community.web.dto.post.ModifyPostDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;      //제목
    private String content;    //내용
    private String image;      //이미지 url
    private int    viewCount;  //뷰 횟수
    private String name;       //작성자 member_name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<MemberPostLike> memberPostLikes = new ArrayList<>();

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.name = member.getName();
        setMember(member);
    }

    public void update(ModifyPostDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.image = dto.getImage();
    }

    public void setMember(Member member){
        this.member = member;
        this.name = member.getName();
        member.getPosts().add(this);
    }


}
