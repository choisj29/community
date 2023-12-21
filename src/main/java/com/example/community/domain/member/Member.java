package com.example.community.domain.member;


import com.example.community.domain.item.Item;
import com.example.community.global.entity.BaseTimeEntity;
import com.example.community.domain.common.Role;
import com.example.community.domain.memberPostLike.MemberPostLike;
import com.example.community.domain.post.Post;
import com.example.community.domain.reply.Reply;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    private String loginId;    //아이디
    private String password;   //비밀번호
    private String name;       //닉네임
    private String image;      //이미지 url
    private boolean isDeleted = false; //탈퇴여부

    @Enumerated(EnumType.STRING)
    private Role role;

    public void addUserAuthority() {
        this.role = Role.User;
    }

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberPostLike> memberPostLikes = new ArrayList<>();

    @Builder
    public Member(String loginId, String password, String name, String image) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.image = image;
        addUserAuthority();
    }

    public void delete() {
        this.isDeleted = true;
    }

}
