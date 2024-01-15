package com.example.community.web.controller.member;

import com.example.community.SessionConst;
import com.example.community.common.BaseControllerTest;
import com.example.community.domain.member.Member;
import com.example.community.service.login.LoginService;
import com.example.community.service.member.MemberService;
import com.example.community.service.post.PostService;
import com.example.community.service.reply.ReplyService;
import com.example.community.web.dto.member.JoinMemberRequest;
import com.example.community.web.dto.member.WithdrawalDto;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends BaseControllerTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    ReplyService replyService;
    @Autowired
    LoginService loginService;

    @Test
    //@Commit
    public void 회원가입테스트() throws Exception{
        //given
        String loginId = "test11";
        String name = "test11";
        String password = "Testtest11!@";
        //when
        ResultActions resultActions = this.mockMvc.perform(
                post("/members/add")
                        .param("loginId",loginId)
                        .param("name", name)
                        .param("password", password)
        );
        //then
        Optional<Member> savedMember = memberService.findByLoginId(loginId);
        Assertions.assertThat(savedMember.get().getName()).isEqualTo(name);
        Assertions.assertThat(savedMember.get().getPassword()).isNotEqualTo(password);
        //password encode check
        Assertions.assertThat(savedMember.get().getLoginId()).isEqualTo(loginId);
    }

    @Test
    public void 회원가입테스트_중복아이디() throws Exception{
        //given
        JoinMemberRequest dto = JoinMemberRequest.builder()
                .loginId("test")
                .password("Testtest11!@")
                .name("test123")
                .build();
        String json = new Gson().toJson(dto);
        ResultActions resultActions = this.mockMvc.perform(
                post("/members/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
        );
        //then
        resultActions.andExpect(model().hasErrors());
    }

    @Test
    public void 회원가입테스트_중복닉네임() throws Exception{
        //given
        JoinMemberRequest dto = JoinMemberRequest.builder()
                .loginId("test123")
                .password("Testtest11!@")
                .name("test1")
                .build();
        String json = new Gson().toJson(dto);
        ResultActions resultActions = this.mockMvc.perform(
                post("/members/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
        );
        //then
        resultActions.andExpect(model().hasErrors());
    }

    @Test
    public void 회원탈퇴테스트_정상() throws Exception{
        String testId = "test";
        String testPw = "test";
        Member loginMember = loginService.login("test", "test");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        //given
        WithdrawalDto dto = WithdrawalDto.builder()
                .loginId(testId)
                .password(testPw)
                .build();
        String json = new Gson().toJson(dto);
        ResultActions resultActions = this.mockMvc.perform(
                post("/members/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
        );
        //then

    }
}