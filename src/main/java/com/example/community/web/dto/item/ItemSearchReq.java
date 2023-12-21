package com.example.community.web.dto.item;

import lombok.Builder;
import lombok.Data;

@Data
public class ItemSearchReq {
    private String searchCondition;
    private String searchKeyword;
    private Long memberId;
}
