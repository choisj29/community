package com.example.community.repository.rankLog;

import com.example.community.domain.rankLog.QRankLog;
import com.example.community.domain.rankLog.RankLog;
import com.example.community.web.dto.item.QRangeKeywordResponse;
import com.example.community.web.dto.item.RangeKeywordRequest;
import com.example.community.web.dto.item.RangeKeywordResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.community.domain.item.QItem.item;
import static com.example.community.domain.keyword.QKeyword.keyword1;
import static com.example.community.domain.rankLog.QRankLog.rankLog;

@Slf4j
@RequiredArgsConstructor
public class RankLogRepositoryImpl implements RankLogRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<RangeKeywordResponse> findWithRange(RangeKeywordRequest req) {
        List<RangeKeywordResponse> results = queryFactory.select(new QRangeKeywordResponse(
                        keyword1.id,
                        keyword1.keyword,
                        rankLog.id,
                        rankLog.itemRank,
                        rankLog.createdTime,
                        item.itemName))
                .from(rankLog)
                .innerJoin(keyword1).on(rankLog.keyword.id.eq(keyword1.id))
                .innerJoin(item).on(keyword1.item.id.eq(item.id))
                .where(
                        rankLog.keyword.id.eq(req.getKeywordId()),
                        betweenDate(req)
                )
                .fetch();
        return results;
    }


    private BooleanExpression betweenDate(RangeKeywordRequest req) {
        BooleanExpression be = null;

        try {
            if (StringUtils.hasText(req.getStart())) { //기간 설정이 있다
                LocalDateTime start = LocalDate.parse(req.getStart()).atStartOfDay();
                LocalDateTime end = StringUtils.hasLength(req.getEnd()) ? LocalDateTime.of(LocalDate.parse(req.getEnd()), LocalTime.MAX).withNano(0) : LocalDate.now().atTime(LocalTime.MAX).withNano(0);
                be = rankLog.createdTime.between(start,end);
            } else { //기간 설정이 없다면 마지막 가록일로부터 7일간의 rank 기록을 보여주기
                QRankLog r = new QRankLog("r");
                RankLog maxIdRankLog = queryFactory
                        .select(rankLog)
                        .from(rankLog)
                        .where(
                                rankLog.id.eq(
                                        JPAExpressions.select(r.id.max())
                                                .from(r)
                                                .innerJoin(keyword1).on(r.keyword.id.eq(keyword1.id))
                                                .where(keyword1.item.id.eq(req.getKeywordId()))))
                        .fetchOne();

                LocalDateTime end = ObjectUtils.isEmpty(maxIdRankLog) ? null : maxIdRankLog.getCreatedTime();

                be = ObjectUtils.isEmpty(end) ? null : rankLog.createdTime.between( end.minusDays(7), end.toLocalDate().atTime(LocalTime.MAX).withNano(0));
            }

        }catch (NullPointerException e){
            //log.info("null!!!!!!");
        }
        return be;
    }
}
