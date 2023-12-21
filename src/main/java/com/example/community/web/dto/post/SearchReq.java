package com.example.community.web.dto.post;

import lombok.Data;

@Data
public class SearchReq {
    private String searchCondition;
    private String searchKeyword;
}
