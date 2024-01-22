package com.example.community.web.controller.post;

import com.example.community.SessionConst;
import com.example.community.domain.common.PageMaker;
import com.example.community.domain.member.Member;
import com.example.community.domain.post.Post;
import com.example.community.domain.reply.Reply;
import com.example.community.service.post.PostService;
import com.example.community.service.reply.ReplyService;
import com.example.community.web.dto.post.*;
import com.example.community.web.dto.reply.SaveReplyDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

import static java.time.LocalDateTime.now;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final ReplyService replyService;

    @GetMapping("/post/new")
    public String createPost(@ModelAttribute SavePostRequest dto){
        return "post/new";
    }

    //글 저장
    @PostMapping("/post/new")
    public String savePost(@ModelAttribute SavePostRequest dto,
                            RedirectAttributes redirectAttributes,
                            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                            Member loginMember) throws IOException {
        log.info("loginMember check {} ", loginMember.getId());
        dto.setMemberInfo(loginMember);
        Long postId  = postService.savePost(dto);

        redirectAttributes.addAttribute("page", 1);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }
    //글 수정
    @GetMapping("/post/{postId}/modify")
    public String createModifyPost(@PathVariable("postId") Long postId,
                                   Model model){
        Post post = postService.findById(postId);
        model.addAttribute("modifyPost", post);
        return "post/modify";
    }

    @PostMapping("/post/{postId}/modify")
    public String modifyPost(@ModelAttribute ModifyPostDto dto,
                             @PathVariable("postId") Long postId,
                             RedirectAttributes redirectAttributes,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){
        log.info("ModifyPostDto {} {}", dto.getTitle(),dto.getContent());
        dto.setMemberId(loginMember.getId());
        postService.modifyPost(postId, dto);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    //글 목록
    @GetMapping("/post/list")
    public String postList(Model model, SearchReq searchReq,
                           @PageableDefault(size=10) Pageable pageable){
        int pageNum = pageable.getPageNumber()+1;
        int pageSize = pageable.getPageSize();
        Page<ListPostDto> paging = postService.search(searchReq, pageable);

        PageMaker pageMaker = new PageMaker(pageNum, paging.getTotalElements(), pageSize, 10);

        model.addAttribute("paging", paging);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("searchReq", searchReq);

        return "post/list";
    }


    //상세페이지 조회수 cron scheduler
    @GetMapping("/post/{postId}")
    public String postView(@PathVariable("postId") Long postId,
                            @RequestParam(value="page", required = false, defaultValue = "1") int page,
                            Model model,
                            RedirectAttributes redirectAttributes,
                            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {

        Post post = postService.findById(postId);

        //댓글 1페이지 일단 고정함
        Page<Reply> paging = replyService.replies(postId,1);
        PageMaker pageMaker = new PageMaker(1, paging.getTotalElements(), paging.getSize(), 5);

        //조회수증가 table
        //memberId , boardId 모두 일치 하는 반환값이 있다면 조회수 증가하지않음
//        List<RecentViewMember> allByIds = recentViewMemberRepository.findAllByIds(loginMember.getId(), boardId);
//        log.info("allByIds size check {}", allByIds.size());
//        if(allByIds.size() == 0){
//            boardService.updateView(boardId);
//            RecentViewMember recentViewMember = new RecentViewMember(loginMember.getId(), boardId, now());
//            recentViewMemberRepository.save(recentViewMember);
//        }

        model.addAttribute("post", post);
        model.addAttribute("replyForm", new SaveReplyDto());
        model.addAttribute("paging", paging);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("member", loginMember);

        redirectAttributes.addAttribute("page", page);
        return "post/view";
    }

    @PostMapping("/post/{postId}/delete")
    @ApiOperation(value = "게시글 삭제")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/post/list";
    }
}
