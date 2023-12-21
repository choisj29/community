package com.example.community.repository.reply;

import com.example.community.domain.reply.Reply;
import com.example.community.web.dto.reply.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {
    List<Reply> findAllByPostId(Long postId);
    Page<Reply> findAllByPostId(Long postId, Pageable pageable);

}
