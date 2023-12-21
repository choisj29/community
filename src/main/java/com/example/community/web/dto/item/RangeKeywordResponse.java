package com.example.community.web.dto.item;

import com.example.community.domain.rankLog.RankLog;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class RangeKeywordResponse {
    private Long keywordId;
    private String keyword;

    private Long rankLogId;
    private int rank;
    private LocalDateTime date;

    private String itemName;


    @QueryProjection
    public RangeKeywordResponse(Long keywordId, String keyword, Long rankLogId, int rank, LocalDateTime date, String itemName) {
        this.keywordId = keywordId;
        this.keyword = keyword;
        this.rankLogId = rankLogId;
        this.rank = rank;
        this.date = date;
        this.itemName = itemName;
    }
}
