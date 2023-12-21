package com.example.community.repository.item;

import com.example.community.domain.keyword.QKeyword;
import com.example.community.domain.rankLog.QRankLog;
import com.example.community.web.dto.item.*;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.community.domain.item.QItem.item;
import static com.example.community.domain.keyword.QKeyword.keyword1;
import static com.example.community.domain.rankLog.QRankLog.rankLog;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ListItemDto> search(ItemSearchReq searchReq, Pageable pageable) {

        List<ListItemDto> results = queryFactory
                .select(new QListItemDto(
                        item.id,
                        item.itemName,
                        item.imgLink,
                        item.createdTime
                )).distinct()
                .from(item)
                .innerJoin(keyword1).on(item.id.eq(keyword1.item.id))
                .where(
                        memberIdEq(searchReq.getMemberId()),
                        searchConditionAndKeywordEq(searchReq.getSearchCondition(), searchReq.getSearchKeyword()))

                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(item.countDistinct())
                .from(item)
                .where(
                        //ltItemId(searchReq.getAfter()),
                        searchConditionAndKeywordEq(searchReq.getSearchCondition(), searchReq.getSearchKeyword())
                );

        //1:N id list
        List<Long> itemIds = new ArrayList<>();

        for (ListItemDto result : results) {
            Long itemId = result.getItemId();
            itemIds.add(itemId);
        }
        List<KeywordWithRankForm> keywordList = findKeywords(itemIds);

        results.stream()
                .forEach(result -> {
                    result.setKeywords(
                            keywordList.stream()
                                    .filter(KeywordWithRankForm -> KeywordWithRankForm.getItemId().equals(result.getItemId()))
                                    .collect(Collectors.toList()));
                });

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private List<KeywordWithRankForm> findKeywords(List<Long> itemIds) {

        List<Long> maxIds = queryFactory
                .select(rankLog.id.max())
                .from(rankLog)
                .innerJoin(keyword1).on(rankLog.keyword.id.eq(keyword1.id))
                .where(keyword1.item.id.in(itemIds))
                .groupBy(rankLog.keyword.id)
                .fetch();


        return queryFactory
                .select(new QKeywordWithRankForm(
                        keyword1.id,
                        keyword1.keyword,
                        rankLog.id,
                        rankLog.itemRank,
                        rankLog.createdTime,
                        keyword1.item.id))
                .from(keyword1)
                .innerJoin(rankLog).on(keyword1.id.eq(rankLog.keyword.id))
                .where(
                        inItemIds(itemIds),
                        maxRankLogIds(maxIds)
                        )
                .fetch();
    }

    private BooleanExpression inItemIds(List<Long> itemIds) {
        return itemIds.isEmpty() ? null : keyword1.item.id.in(itemIds);
    }

    private BooleanExpression maxRankLogIds(List<Long> logIds) {
        return logIds.isEmpty() ? null : rankLog.id.in(logIds);
    }

    private BooleanExpression ltItemId(Long itemId) {
        log.info("itemId is {}", itemId);
        return itemId == null ? null : item.id.lt(itemId);
    }


    private BooleanExpression searchConditionAndKeywordEq(String searchCondition, String searchKeyword) {
        BooleanExpression searchConditionExpression=null;
        log.info("ItemSearchReq.getSearchCondition() {}",searchCondition);
        log.info("ItemSearchReq.searchKeyword() {}",searchKeyword);
        try {
            if (searchCondition.equals("itemName")) {
                searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : item.itemName.contains(searchKeyword);
            } else if (searchCondition.equals("mid")) {
                searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : item.mid.contains(searchKeyword);
            } else if (searchCondition.equals("keyword")) {
                searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : keyword1.keyword.contains(searchKeyword);
            }
        }catch (NullPointerException e){
            //log.info("null!!!!!!");
        }
        return searchConditionExpression;
    }


    private BooleanExpression memberIdEq(Long memberId){
        return StringUtils.isEmpty(memberId) ? item.member.id.eq(0L) : item.member.id.eq(memberId);
    }




}
