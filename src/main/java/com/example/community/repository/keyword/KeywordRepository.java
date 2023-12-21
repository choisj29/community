package com.example.community.repository.keyword;

import com.example.community.domain.item.Item;
import com.example.community.domain.keyword.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Keyword findKeywordByKeywordAndItem(String keyword, Item item);
}
