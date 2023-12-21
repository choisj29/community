package com.example.community.repository.item;

import com.example.community.web.dto.item.ItemSearchReq;
import com.example.community.web.dto.item.ListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<ListItemDto> search(ItemSearchReq searchReq, Pageable pageable);
}
