package com.example.community.repository.reply;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;


@Slf4j
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{
    /**
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReplyDto> findListByPostId(Long postId, AllReplyCondition condition, Pageable pageable) {
        List<ReplyDto> results = queryFactory
                .select(new QReplyDto(
                        reply.id,
                        reply.content,
                        reply.memberReplyLikes.size(),
                        reply.subReplies.size(),
                        reply.writeTime,
                        reply.name,
                        member.image,
                        reply.isDeleted))
                .from(reply)
                .leftJoin(member).on(reply.member.id.eq(member.id))
                .where(
                        reply.post.id.eq(postId),
                        ltReplyId(condition.getAfter())
                )
                .orderBy(reply.id.desc())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(reply)
                .where(
                        reply.post.id.eq(postId),
                        ltReplyId(condition.getAfter())
                );

        results.stream()
                .forEach(result -> {
                    if (result.isDeleted()) {
                        result.setContent("");
//                        result.setLikeCount(0);
                    }
                });

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchOne());
    }



    //after
    //replyId를 통해 인덱싱
    private BooleanExpression ltReplyId(Long replyId) {
        log.info("replyId is {}", replyId);
        return replyId == null ? null : reply.id.lt(replyId);
    }
**/

}
