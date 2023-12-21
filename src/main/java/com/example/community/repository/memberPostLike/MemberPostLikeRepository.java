package com.example.community.repository.memberPostLike;

import com.example.community.domain.member.Member;
import com.example.community.domain.memberPostLike.MemberPostLike;
import com.example.community.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPostLikeRepository extends JpaRepository<MemberPostLike, Long> {
    MemberPostLike findByPostAndMember(Post post, Member member);

}
