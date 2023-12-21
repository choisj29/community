package com.example.community.web.dto.item;

import com.example.community.domain.item.Item;
import com.example.community.domain.keyword.Keyword;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class ListItemDto {

    private Long itemId;
    private String itemName;
    private String itemImgLink; //아이템 이미지 링크
    private LocalDateTime regDate; //아이템등록일

    private List<KeywordWithRankForm> keywords = new ArrayList<>();

    @QueryProjection
    public ListItemDto(Long id, String itemName, String itemImgLink, LocalDateTime regDate) {
        this.itemId = id;
        this.itemName = itemName;
        this.itemImgLink = itemImgLink;
        this.regDate = regDate;
    }
}
