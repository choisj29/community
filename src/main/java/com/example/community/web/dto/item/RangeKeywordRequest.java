package com.example.community.web.dto.item;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class RangeKeywordRequest {
    private Long keywordId;

    private String start;

    private String end;
}
