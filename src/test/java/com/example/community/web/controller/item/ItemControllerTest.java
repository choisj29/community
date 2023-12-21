package com.example.community.web.controller.item;

import com.example.community.SessionConst;
import com.example.community.common.BaseControllerTest;
import com.example.community.domain.item.Item;
import com.example.community.domain.keyword.Keyword;
import com.example.community.domain.member.Member;
import com.example.community.service.item.ItemService;
import com.example.community.service.keyword.KeywordService;
import com.example.community.service.member.MemberService;
import com.example.community.service.rankLog.RankLogService;
import com.example.community.web.dto.item.SaveItemDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class ItemControllerTest extends BaseControllerTest {
    @Autowired
    ItemService itemService;
    @Autowired
    KeywordService keywordService;
    @Autowired
    MemberService memberService;
    @Autowired
    RankLogService rankLogService;

    @Test
    public void 아이템저장테스트() throws Exception{
        //given
        Long memberId = 1L;
        Member loginMember = memberService.findById(memberId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        List<String> keywords = new ArrayList<>();
        keywords.add("강아지 간식");
        keywords.add("애견 간식");

        String mid = "1234";
//        ItemSearchReq req = ItemSearchReq.builder()
//                .searchCondition("mid")
//                .searchKeyword("")
//                .build();

        SaveItemDto dto = SaveItemDto.builder()
                .mid(mid)
                .keywords(keywords)
                .build();
        String json = new Gson().toJson(dto);
        //when
        ResultActions resultActions = this.mockMvc.perform(
                post("/item/save")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
        );

        resultActions.andExpect(status().is(200))
                .andDo(print());
    }

    @Test
    @DisplayName("아이템이 삭제되면 연관 키워드도 삭제되어야 한다")
    public void 아이템삭제테스트() throws Exception{
        Long itemId = 10L;
        Long memberId = 1L;
        Member loginMember = memberService.findById(memberId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        ResultActions resultActions = this.mockMvc.perform(
                post("/item/"+itemId+"/delete")
                        .session(session)
        );

        RuntimeException runtimeException = assertThrows(RuntimeException.class,() -> itemService.findById(itemId));
        List<Keyword> keyword = keywordService.findKeyword(itemId);
        Assertions.assertThat(keyword).isEmpty();
    }

    @Test
    @DisplayName("본인이 등록한 아이템이 아니면 삭제 불가")
    public void 아이템삭제_실패테스트() throws Exception{
        Long itemId = 7L;
        Long memberId = 5L;
        Member loginMember = memberService.findById(memberId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        ResultActions resultActions = this.mockMvc.perform(
                post("/item/"+itemId+"/delete")
                        .session(session)
        );
        Item item = itemService.findById(itemId);
        Assertions.assertThat(item).isNotNull();
    }

    @Test
    public void 아이템검색테스트() throws Exception{
        //given
        Long memberId = 1L;
        String mid = "13007183296";
        Member loginMember = memberService.findById(memberId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        //when
        ResultActions resultActions = this.mockMvc.perform(
                get("/item/list")
                        .session(session)
                        .param("searchCondition", "mid")
                        .param("searchKeyword", mid)
        );

        resultActions.andExpect(status().is(200))
                .andDo(print());
    }
}