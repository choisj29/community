package com.example.community.web.dto.rankLog;

import com.example.community.domain.rankLog.RankLog;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class RankLogSpecification {
    public static Specification<RankLog> equalKeywordId(Long keywordId) {
        return new Specification<RankLog>() {
            @Override
            public Predicate toPredicate(Root<RankLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("keyword").get("id"), keywordId);
            }
        };
    }

    public static Specification<RankLog> betweenDate(LocalDateTime strDate, LocalDateTime endDate) {
        return new Specification<RankLog>() {
            @Override
            public Predicate toPredicate(Root<RankLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("createdTime"), strDate, endDate);
            }
        };
    }
}