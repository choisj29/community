package com.example.community.service.reply;

import com.example.community.domain.member.Member;
import com.example.community.domain.post.Post;
import com.example.community.domain.reply.Reply;
import com.example.community.repository.reply.ReplyRepository;
import com.example.community.service.member.MemberService;
import com.example.community.service.post.PostService;
import com.example.community.web.dto.reply.AllReplyCondition;
import com.example.community.web.dto.reply.ModifyReplyDto;
import com.example.community.web.dto.reply.ReplyDto;
import com.example.community.web.dto.reply.SaveReplyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final MemberService memberService;
    private final PostService postService;
    @Transactional
    public Reply saveReply(SaveReplyDto dto) {
        Member member = memberService.findById(dto.getMemberId());
        Post post = postService.findById(dto.getPostId());
        Reply reply = new Reply(dto.getContent(), LocalDateTime.now(), post, member, member.getName());
        return replyRepository.save(reply);
    }

    public List<ReplyDto> findAllByPostId(Long postId) {
        List<Reply> findAll = replyRepository.findAllByPostId(postId);
        List<ReplyDto> replyDtos = new ArrayList<>();
        findAll.forEach(reply -> replyDtos.add(new ReplyDto(reply)));
        return replyDtos;
    }

//    public Page<ReplyDto> findListByPostId(Long postId, AllReplyCondition condition, Pageable pageable) {
//        return replyRepository.findListByPostId(postId, condition, pageable);
//
//    }

    public Reply findById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(() -> new RuntimeException("reply is null"));
    }

    @Transactional
    public void deleteReply(Long replyId) {
        Reply reply = this.findById(replyId);
        replyRepository.delete(reply);
    }

    @Transactional
    public ReplyDto modifyReply(Long replyId, ModifyReplyDto dto) {
        Reply reply = findById(replyId);
        if (!reply.isDeleted()) {
            reply.modify(dto.getContent());
        }
        return new ReplyDto(reply);
    }

    //댓글목록 + 페이징
    public Page<Reply> replies(Long postId, int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("writeTime"));
        //index 0 조정
        if(page <= 0) page=1;
        Pageable pageable = PageRequest.of(page-1, 5, Sort.by(sorts));
        return replyRepository.findAllByPostId(postId, pageable);

    }
}
