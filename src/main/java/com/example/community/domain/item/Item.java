package com.example.community.domain.item;

import com.example.community.domain.keyword.Keyword;
import com.example.community.domain.member.Member;
import com.example.community.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long    id;
    private String  mid;        //상품고유id
    private String  itemName;   //상품명
    @Column(length = 1000)
    private String  imgLink;    //상품이미지링크

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Keyword> keywords = new ArrayList<>();

    public void setMember(Member member){
        this.member = member;
        member.getItems().add(this);
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Item(String mid, Member member) {
        this.mid = mid;
        setMember(member);
    }
}
