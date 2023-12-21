package com.example.community.repository.rankLog;

import com.example.community.domain.rankLog.RankLog;
import com.example.community.web.dto.item.RangeKeywordRequest;
import com.example.community.web.dto.item.RangeKeywordResponse;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public interface RankLogRepositoryCustom {
    List<RangeKeywordResponse> findWithRange(RangeKeywordRequest req);
}
