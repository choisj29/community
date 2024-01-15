package com.example.community.web.controller.reply;

import com.example.community.common.BaseControllerTest;
import com.example.community.domain.reply.Reply;
import com.example.community.service.reply.ReplyService;
import com.example.community.web.dto.reply.ModifyReplyDto;
import com.example.community.web.dto.reply.ReplyDto;
import com.example.community.web.dto.reply.SaveReplyDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class ReplyControllerTest extends BaseControllerTest {
    @Autowired
    ReplyService replyService;

    @Test
//    @Commit
    public void 댓글등록테스트() throws Exception{
        //given
        SaveReplyDto dto = new SaveReplyDto(1L, 3L, "reply save test");
        //when
        ResultActions resultActions = this.mockMvc.perform(
                post("/reply/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto))
        );
        //then
        resultActions.andExpect(status().is(200))
                .andExpect(jsonPath("$[0].content").value("reply save test"));
    }


    @Test
//    @Commit
    public void 댓글수정테스트() throws Exception{
        //given
        ModifyReplyDto dto = new ModifyReplyDto("modify reply");
        Long replyId = 1L;
        //when
        ResultActions resultActions = this.mockMvc.perform(
                ///reply/{replyId}/modify
                post("/reply/"+replyId+"/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto))
        );
        //then
        resultActions.andExpect(status().is(200))
                .andExpect(jsonPath("$[0].content").value("modify reply"));
    }

    @Test
//    @Commit
    public void 댓글삭제테스트() throws Exception{
        Long replyId = 1L;
        Reply reply = replyService.findById(replyId);
        ReplyDto dto = new ReplyDto(reply);
        ResultActions resultActions = this.mockMvc.perform(
                post("/reply/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto))
        );

        //then
        resultActions.andExpect(status().is(200))
                .andExpect(jsonPath("$.deleted").value("deleted"));
    }
}