package com.example.community.web.dto.rankLog;

import lombok.Data;

@Data
public class RenewRankForm {
    private Long itemId;
    private String renewDate; //순위 갱신일
    private int rank; //순위
    private String keyword;
    private String imgLink;
//    private String itemLink;
    private String itemName;
    private Long keywordId;
}
