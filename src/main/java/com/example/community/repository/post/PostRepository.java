package com.example.community.repository.post;

import com.example.community.domain.post.Post;
import com.example.community.web.dto.post.ListPostDto;
import com.example.community.web.dto.post.SearchReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}
