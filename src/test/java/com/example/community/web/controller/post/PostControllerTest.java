package com.example.community.web.controller.post;

import com.example.community.SessionConst;
import com.example.community.common.BaseControllerTest;
import com.example.community.domain.member.Member;
import com.example.community.domain.post.Post;
import com.example.community.service.login.LoginService;
import com.example.community.service.post.PostService;
import com.example.community.service.reply.ReplyService;
import com.example.community.web.dto.member.JoinMemberRequest;
import com.example.community.web.dto.post.ModifyPostDto;
import com.example.community.web.dto.post.SavePostRequest;
import com.example.community.web.dto.reply.ReplyDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class PostControllerTest extends BaseControllerTest {
    @Autowired
    LoginService loginService;
    @Autowired
    PostService postService;
    @Autowired
    ReplyService replyService;


    @Test
    public void 게시글저장테스트() throws Exception{
        //given
        Member loginMember = loginService.login("test", "test");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        SavePostRequest dto = SavePostRequest.builder()
                .title("title")
                .memberId(loginMember.getId())
                .name(loginMember.getName())
                .content("content")
                .build();
        String json = new Gson().toJson(dto);

        ResultActions resultActions = this.mockMvc.perform(
                post("/post/new")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
        );
        //then
        resultActions.andDo(print());

    }

    @Test
    public void 게시글수정테스트() throws Exception{
        //given
        Member loginMember = loginService.login("test", "test");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        Long postId = 3L;

        ModifyPostDto dto = ModifyPostDto.builder()
                .postId(postId)
                .title("modify title")
                .content("modify content")
                .memberId(loginMember.getId())
                .build();
        String json = new Gson().toJson(dto);
        log.info("json is {}", json); //TODO
        //when
        ResultActions resultActions = this.mockMvc.perform(
                post("/post/"+postId+"/modify")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
        );

        //then
        Post post = postService.findById(postId);
        Assertions.assertThat(post.getTitle()).isEqualTo("modify title");
        Assertions.assertThat(post.getContent()).isEqualTo("modify content");
    }

    @Test
    @DisplayName("게시글이 삭제되면 댓글도 삭제되어야 한다.")
    public void 게시글삭제테스트() throws Exception{
        Long postId = 3L;
        ResultActions resultActions = this.mockMvc.perform(
                post("/post/"+postId+"/delete")
        );

        RuntimeException runtimeException = assertThrows(RuntimeException.class,() -> postService.findById(postId));
        List<ReplyDto> allByPostId = replyService.findAllByPostId(postId);
        Assertions.assertThat(allByPostId).isEmpty();
    }

}