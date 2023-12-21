package com.example.community.service.keyword;

import com.example.community.domain.item.Item;
import com.example.community.domain.keyword.Keyword;
import com.example.community.repository.keyword.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {
    private final KeywordRepository keywordRepository;

    //키워드 등록
    @Transactional
    public Keyword save(Keyword keyword){
        return keywordRepository.save(keyword);
    }

    //키워드 조회 여러개...
    public List<Keyword> findKeyword(Long itemId){
        return keywordRepository.findAll().stream()
                .filter(k -> k.getItem().getId().equals(itemId))
                .collect(Collectors.toList());
    }

    //find by id
    public Keyword findById(Long keywordId){
        return keywordRepository.findById(keywordId)
                .orElseThrow(() -> new RuntimeException("keyword is null"));
    }

    @Transactional
    public void cancel(Long keywordId){
        keywordRepository.deleteById(keywordId);
    }

    public Keyword findKeywordByKeywordAndItem(String keyword, Item item) {
        return keywordRepository.findKeywordByKeywordAndItem(keyword, item);
    }

}
