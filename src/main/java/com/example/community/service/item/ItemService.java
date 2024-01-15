package com.example.community.service.item;

import com.example.community.domain.item.Item;
import com.example.community.domain.keyword.Keyword;
import com.example.community.domain.member.Member;
import com.example.community.domain.rankLog.RankLog;
import com.example.community.domain.receiver.QueueManager;
import com.example.community.repository.item.ItemRepository;
import com.example.community.service.keyword.KeywordService;
import com.example.community.service.rankLog.RankLogService;
import com.example.community.web.dto.item.ItemSearchReq;
import com.example.community.web.dto.item.ListItemDto;
import com.example.community.web.dto.rankLog.RenewForm;
import com.example.community.web.dto.rankLog.RenewRankForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final RankLogService rankLogService;
    private final KeywordService keywordService;

    public Page<ListItemDto> search(ItemSearchReq searchReq, Pageable pageable) {
        return itemRepository.search(searchReq, pageable);
    }

    @Transactional
    public Item save(Item item) {
       return itemRepository.save(item);
    }

    //find by id
    public Item findById(Long itemId){
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("item is null"));
    }

    @Transactional
    public String renewRank(RenewForm form) throws IOException, InterruptedException {
        System.out.println("renewRank 트랜잭션");
        log.info("RENEW rankLog in ---");
        log.info("form.getItemId {} form.getMid {} form.getMemberId {}", form.getItemId(), form.getMid(), form.getMemberId());

        ObjectMapper mapper = new ObjectMapper();
        List<Object> returnJsons = new ArrayList<>();

        Item item = this.findById(form.getItemId());
        String mid = (form.getItemId().equals(0L)) ? form.getMid() : item.getMid();

        List<Object> queue = QueueManager.getQueue(mid, form.getKeyword());

        List<RenewRankForm> itemThreads = mapper.readValue(String.valueOf(queue), new TypeReference<List<RenewRankForm>>(){});
        RenewRankForm resultItem = itemThreads.get(0);

        if(!form.getItemId().equals(0L)){
            if(!item.getMember().getId().equals(form.getMemberId())){
                return "[NoPermission]권한이 없습니다.";
            }else{
                if (resultItem.getRank() > 0) { //item imgLink, itemName 갱신
                    item.setImgLink(resultItem.getImgLink());
                    item.setItemName(resultItem.getItemName());
                } else {
                    item.setImgLink("");
                    item.setItemName("");
                }
                Keyword findKeyword = keywordService.findKeywordByKeywordAndItem(form.getKeyword(), item);
                RankLog rankLog = new RankLog(resultItem.getRank(), findKeyword);
                resultItem.setKeywordId(findKeyword.getId());
                resultItem.setItemId(item.getId());
                rankLogService.save(rankLog);
                returnJsons.add(resultItem);
            }
        }

        returnJsons.add(resultItem);

        log.info("renewRank return json = {}", mapper.writeValueAsString(returnJsons));
        return mapper.writeValueAsString(returnJsons);
    }

    @Transactional
    public void delete(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
