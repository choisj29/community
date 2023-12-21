package com.example.community.web.dto.item;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SaveItemDto {

    private Long itemId;

    @NotEmpty
    private String mid;

    @NotEmpty
    private List<String> keywords = new ArrayList<>();

    private int page; //현재페이지
    @Builder
    public SaveItemDto(String mid, List<String> keywords) {
        this.mid = mid;
        this.keywords = keywords;
    }
}
