package com.example.community.domain.rankLog;

import com.example.community.domain.keyword.Keyword;
import com.example.community.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RankLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    private int itemRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;


    //연관관계 편의 매소드
    public void setKeyword(Keyword keyword){
        this.keyword = keyword;
        keyword.getRankLogs().add(this);
    }

    public RankLog(int itemRank, Keyword keyword) {
        this.itemRank = itemRank;
        setKeyword(keyword);
    }
}
