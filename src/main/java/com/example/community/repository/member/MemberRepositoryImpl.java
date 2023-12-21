package com.example.community.repository.member;

import com.example.community.web.dto.member.ListMemberDto;
import com.example.community.web.dto.member.MemberSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.community.domain.member.QMember.member;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

//    @Override
//    public Page<ListMemberDto> search(MemberSearchCondition memberCondition, Pageable pageable) {
//        List<ListMemberDto> results = queryFactory
//                .select(new QListMemberDto(
//                        member.id,
//                        member.loginId,
//                        member.name))
//                .from(member)
//                .where(searchConditionAndKeywordEq(memberCondition.getSearchCondition(), memberCondition.getSearchKeyword()))
//                .orderBy(member.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//        JPAQuery<Long> countQuery = queryFactory.select(Wildcard.count)
//                .from(member)
//                .where(searchConditionAndKeywordEq(memberCondition.getSearchCondition(), memberCondition.getSearchKeyword()));
//
//        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchOne());
//    }
//
//
//    private BooleanExpression searchConditionAndKeywordEq(String searchCondition, String searchKeyword) {
//        BooleanExpression searchConditionExpression=null;
//
//        if(StringUtils.isEmpty(searchCondition)) {
//            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : member.loginId.contains(searchKeyword).or(member.name.contains(searchKeyword));
//        } else if (searchCondition.equals("all")) {
//            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : member.loginId.contains(searchKeyword).or(member.name.contains(searchKeyword));
//        } else if (searchCondition.equals("loginId")) {
//            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : member.loginId.contains(searchKeyword);
//        } else if (searchCondition.equals("name")) {
//            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : member.name.contains(searchKeyword);
//        }
//        return searchConditionExpression;
//    }
}
