package com.example.community.web.dto.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeywordWithRankForm {
    private Long keywordId;
    private String keyword;
    private Long rankId;
    private Integer rank;
    private LocalDateTime renewDate;
    private Long itemId;

    @QueryProjection
    public KeywordWithRankForm(Long keywordId, String keyword, Long rankId, Integer rank, LocalDateTime renewDate, Long itemId){
        this.keywordId = keywordId;
        this.keyword = keyword;
        this.rankId = rankId;
        this.rank = rank;
        this.renewDate = renewDate;
        this.itemId = itemId;
    }
}
