package com.example.community.service.memberPostLike;

import com.example.community.domain.member.Member;
import com.example.community.domain.memberPostLike.MemberPostLike;
import com.example.community.domain.post.Post;
import com.example.community.repository.memberPostLike.MemberPostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberPostLikeService {
    private final MemberPostLikeRepository memberPostLikeRepository;

    @Transactional
    public Long save(MemberPostLike memberPostLike) {
        return memberPostLikeRepository.save(memberPostLike).getId();
    }

    @Transactional
    public void delete(MemberPostLike memberPostLike) {
        memberPostLikeRepository.delete(memberPostLike);
    }

    public MemberPostLike findByPostAndMember(Post post, Member member) {
        return memberPostLikeRepository.findByPostAndMember(post, member);
    }

}
