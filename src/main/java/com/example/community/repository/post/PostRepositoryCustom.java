package com.example.community.repository.post;

import com.example.community.web.dto.post.ListPostDto;
import com.example.community.web.dto.post.PostCondition;
import com.example.community.web.dto.post.SearchReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<ListPostDto> search(SearchReq searchReq, Pageable pageable);

    //Page<ListPostDto> searchNoOffset(PostCondition postCondition, Pageable pageable);

    //Page<MyPostDto> findListByMemberId(Long memberId, MyPageCondition condition, Pageable pageable);
}
