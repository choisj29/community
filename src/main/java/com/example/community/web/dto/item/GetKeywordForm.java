package com.example.community.web.dto.item;

import com.example.community.domain.keyword.Keyword;
import lombok.Data;

@Data
public class GetKeywordForm {
    Long keywordId;
    String keyword;

    public GetKeywordForm(Keyword keyword) {
        this.keywordId = keyword.getId();
        this.keyword = keyword.getKeyword();
    }
}
