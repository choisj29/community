package com.example.community.web.dto.rankLog;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RenewForm {
    private Long itemId;
    private String keyword;

    private Long memberId;
    private String mid;

}
