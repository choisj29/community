package com.example.community.service.rankLog;

import com.example.community.domain.rankLog.RankLog;
import com.example.community.repository.rankLog.RankLogRepository;
import com.example.community.web.dto.item.RangeKeywordRequest;
import com.example.community.web.dto.item.RangeKeywordResponse;
import com.example.community.web.dto.rankLog.RankLogSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankLogService {
    private final RankLogRepository rankLogRepository;

    //로그 등록
    @Transactional
    public RankLog save(RankLog rankLog){
       return rankLogRepository.save(rankLog);
    }

    //키워드 관련 로그 모두 조회
    public List<RankLog> findLogs(Long keywordId){
        return rankLogRepository.findAll().stream()
                .filter(r -> r.getKeyword().getId().equals(keywordId))
                .collect(Collectors.toList());
    }

    //키워드 관련 로그 + 기간설정 //specification
    public List<RankLog> findByRange(Long keywordId, LocalDateTime strDate, LocalDateTime endDate){
        Specification<RankLog> spec = Specification.where(RankLogSpecification.equalKeywordId(keywordId));
        if(strDate != null && endDate != null){
            spec = spec.and(RankLogSpecification.betweenDate(strDate,endDate));
        }
        return rankLogRepository.findAll(spec);
    }

    //키워드 관련 로그 + 기간설정 //querydsl
    public List<RangeKeywordResponse> findWithRange(RangeKeywordRequest req) {
        return rankLogRepository.findWithRange(req);
    }

    //로그 1개 조회
    public RankLog findById(Long rankLogId){
        return rankLogRepository.findById(rankLogId).orElseThrow(() -> new RuntimeException("rankLog is null"));
    }



}
