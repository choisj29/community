package com.example.community.web.dto.post;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostCondition {

    private Long after;

    @ApiParam(value = "검색 분류")
    private String searchCondition;

    @ApiParam(value = "검색 키워드")
    private String searchKeyword;

    @ApiParam(value = "태그 검색")
    private List<String> tags = new ArrayList<>();

    @ApiParam(value = "친구피드보기 T/F")
    private Boolean isFriend = false;

    @ApiParam(value = "친구피드보기 T라면 팔로우한 profileId List")
    private List<Long> followingProfileIds = new ArrayList<>();

    @ApiParam(value = "생물종류1 id", required = true)
    private Long categoryId;
}
