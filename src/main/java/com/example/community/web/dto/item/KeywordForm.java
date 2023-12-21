package com.example.community.web.dto.item;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeywordForm {
    private Long   keywordId;
    private String date;
    private int rank;
    private String itemName;
    private String keyword;

    private LocalDateTime strDate;
    private LocalDateTime endDate;

    private String strDateString;
    private String endDateString;

    private Long itemId;
    private Long rankLogId;
}
