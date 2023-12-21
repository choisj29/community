package com.example.community.web.controller.reply;

import com.example.community.SessionConst;
import com.example.community.domain.common.PageMaker;
import com.example.community.domain.member.Member;
import com.example.community.domain.post.Post;
import com.example.community.domain.reply.Reply;
import com.example.community.service.member.MemberService;
import com.example.community.service.post.PostService;
import com.example.community.service.reply.ReplyService;
import com.example.community.web.dto.reply.ModifyReplyDto;
import com.example.community.web.dto.reply.ReplyDto;
import com.example.community.web.dto.reply.SaveReplyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    private final MemberService memberService;
    private final PostService postService;

    //댓글저장
    @PostMapping("/reply/save")
    @ResponseBody
    @ApiOperation(value = "댓글 저장")
    public String saveReply(@RequestBody SaveReplyDto dto) throws JsonProcessingException {
        log.info("saveReply dto {} {} {}", dto.getContent(), dto.getPostId(), dto.getPostId());

        List<Object> returnJsons = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Reply saved = replyService.saveReply(dto);

        returnJsons.add(new ReplyDto(saved));
        String json = mapper.writeValueAsString(returnJsons);
        log.info("saveReply return json = {}",json);
        return json;
    }

//    @PostMapping("/api/reply/save")
//    @ApiOperation(value = "댓글 저장")
//    public ReplyDto saveReplyAPI(@RequestBody SaveReplyDto dto) {
//        log.info("saveReply dto {} {} {}", dto.getContent(), dto.getPostId(), dto.getPostId());
//
//        Member member = memberService.findById(dto.getMemberId());
//        Post post = postService.findById(dto.getPostId());
//        Reply reply = new Reply(dto.getContent(), LocalDateTime.now(), post, member, member.getName());
//        Reply saved = replyService.saveReply(reply);
//
//        return new ReplyDto(saved);
//    }

    //댓글목록
    @PostMapping("/post/{postId}/reply")
    @ResponseBody
    public String getReplyList(@PathVariable("postId") Long postId,
                               @RequestBody ReplyDto dto ) throws JsonProcessingException {
        log.info("replyController getReplyList in----");
        Page<Reply> paging = replyService.replies(postId, dto.getReplyPage());

        if(dto.getReplyPage() == 0) dto.setReplyPage(1);
        log.info("replyPage = {}",dto.getReplyPage());

        List<Object> returnJsons = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PageMaker pageMaker = new PageMaker(dto.getReplyPage(), paging.getTotalElements(), 5,5);
        returnJsons.add(pageMaker);

        for (Reply reply : paging) {
            ReplyDto replies = new ReplyDto(reply);
            returnJsons.add(replies);
        }

        String json = mapper.writeValueAsString(returnJsons);
        log.info("getReplyList return json = {}",json);

        return json;
    }

    //댓글 삭제 ajax
    @PostMapping("/reply/delete")
    @ResponseBody
    public String deleteReply(@RequestBody ReplyDto dto){
        log.info("reply cancel in--");
        log.info("ReplyForm is {} {} {} {}", dto.getReplyId(), dto.getPostId(), dto.getReplyPage(), dto.getMemberId());

        //남은 댓글 개수가 0이라면 전체 댓글 목록 remove -> 목록 갱신 페이징까지 끌어오기
        //댓글 삭제
        replyService.deleteReply(dto.getReplyId());

        List<Reply> replyList = postService.findById(dto.getPostId()).getReplies();
        int size = replyList.size();

        log.info("remain size is {}", size);

        JsonObject jo = new JsonObject();
        jo.addProperty("deleted", "deleted");
        jo.addProperty("replyCnt", size);

        log.info("to string = {}", jo.toString());
        return jo.toString();
    }

    //댓글 수정
    @PostMapping("/reply/{replyId}/modify")
    @ResponseBody
    public String replyModify(@PathVariable Long replyId,
                              @RequestBody ModifyReplyDto dto,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws JsonProcessingException {
        log.info("replyModify ReplyForm is {}", dto.getContent());

        List<Object> returnJsons = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ReplyDto modified = replyService.modifyReply(replyId, dto);
        returnJsons.add(modified);
        String json = mapper.writeValueAsString(returnJsons);
        log.info("modify Reply return json = {}",json);
        return json;

    }

}
