package com.example.community.repository.rankLog;

import com.example.community.domain.rankLog.RankLog;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankLogRepository extends JpaRepository<RankLog, Long>, JpaSpecificationExecutor<RankLog>, RankLogRepositoryCustom {

    List<RankLog> findAll(Specification<RankLog> spec);
}