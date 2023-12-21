package com.example.community.repository.item;

import com.example.community.domain.item.Item;
import com.example.community.domain.member.Member;
import com.example.community.web.dto.item.ItemSearchReq;
import com.example.community.web.dto.item.ListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Page<Item> findAllByMemberId(Long memberId, Pageable pageable);

    Page<Item> findAll(Specification<Item> spec, Pageable pageable);

}
