package com.example.community.repository.member;

import com.example.community.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByLoginId(String LoginId);

    boolean existsByName(String name);

    boolean existsByLoginId(String loginId);
}
