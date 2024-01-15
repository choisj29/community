package com.example.community.repository.post;

import com.example.community.web.dto.post.ListPostDto;
import com.example.community.web.dto.post.PostCondition;
import com.example.community.web.dto.post.QListPostDto;
import com.example.community.web.dto.post.SearchReq;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.community.domain.post.QPost.post;


@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ListPostDto> search(SearchReq searchReq, Pageable pageable) {
        QueryResults result = queryFactory
                .select(new QListPostDto(
                        post.id,
                        post.title,
                        post.content,
                        post.replies.size(),
                        post.viewCount,
                        post.createdTime,
                        post.member.id,
                        post.member.name,
                        post.member.image))
                .from(post)
                //.leftJoin(board.member,member)
                .where(searchConditionAndKeywordEq(searchReq.getSearchCondition(), searchReq.getSearchKeyword()))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(),pageable, result.getTotal());
    }

    private BooleanExpression searchConditionAndKeywordEq(String searchCondition, String searchKeyword) {
        BooleanExpression searchConditionExpression=null;
        log.info("searchReq.getSearchCondition() {}",searchCondition);
        log.info("searchReq.searchKeyword() {}",searchKeyword);
        if(StringUtils.isEmpty(searchCondition)){
            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : post.title.contains(searchKeyword).or(post.content.contains(searchKeyword));
        }else if(searchCondition.equals("all")){
            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : post.title.contains(searchKeyword).or(post.content.contains(searchKeyword));
        }else if(searchCondition.equals("title")){
            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : post.title.contains(searchKeyword);
        }else if(searchCondition.equals("content")){
            searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : post.content.contains(searchKeyword);
        }
        return searchConditionExpression;
    }

/**
    @Override
    public Page<ListPostDto> searchNoOffset(PostCondition postCondition, Pageable pageable) {
        List<ListPostDto> results = queryFactory
                .select(new QListPostDto(
                        post.id,
                        post.title,
                        post.content,
                        post.replies.size(),
                        post.memberPostLikes.size(),
                        post.viewCount,
                        post.writeTime,
                        member.id,
                        member.name,
                        member.image
                ))
                .from(post).distinct()
                .innerJoin(profilePost).on(post.id.eq(profilePost.post.id))
                .innerJoin(profile).on(profilePost.profile.id.eq(profile.id))
                .innerJoin(postTag).on(postTag.post.id.eq(post.id))
                .innerJoin(tag1).on(postTag.tag.id.eq(tag1.id))
                .innerJoin(postSpecify1).on(post.id.eq(postSpecify1.post.id))
                .innerJoin(specify1).on(postSpecify1.specify1.id.eq(specify1.id))
                .leftJoin(member).on(profile.member.id.eq(member.id))
                .where(
                        ltPostId(postCondition.getAfter()),
                        eqSpecify1(postCondition.getCategoryId()),
                        searchCondition(postCondition.getSearchCondition(), postCondition.getSearchKeyword()),
                        likeTags(postCondition.getTags()),
                        eqFollowedFriends(postCondition.getIsFriend(), postCondition.getFollowingProfileIds())
                )
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.countDistinct())
                .from(post)
                .innerJoin(postSpecify1).on(post.id.eq(postSpecify1.post.id))
                .where(
                        ltPostId(postCondition.getAfter()),
                        eqSpecify1(postCondition.getCategoryId()),
                        searchCondition(postCondition.getSearchCondition(), postCondition.getSearchKeyword()),
                        likeTags(postCondition.getTags()),
                        eqFollowedFriends(postCondition.getIsFriend(), postCondition.getFollowingProfileIds())
                );

        //post images from photo
        List<Photo> photos = queryFactory.selectFrom(photo).fetch();

        //태그정보 or id list
        List<Long> postIds = new ArrayList<>();

        for (ListPostDto result : results) {
            Long postId = result.getPostId();
            log.info("postIds {}", postId);
            postIds.add(postId);
        }

        //profile post
        List<ProfilePostDto> profilePosts = profilePosts(postIds);
        List<ListBadgeDto> memberBadges = queryFactory.select(new QListBadgeDto(
                        member.id,
                        badge.id,
                        badge.name
                )).from(memberBadge)
                .innerJoin(badge).on(memberBadge.badge.id.eq(badge.id))
                .innerJoin(member).on(memberBadge.member.id.eq(member.id))
                .fetch();

        //태그정보 or 쿼리
        Set<TagDto> tagList = queryFactory
                .select(new QTagDto(
                        postTag.post.id,
                        tag1.tag
                )).from(tag1)
                .leftJoin(postTag).on(postTag.tag.id.eq(tag1.id))
                .where(inPostTagPostIds(postIds))
                .stream().collect(Collectors.toSet());


        results.stream()
                .forEach(result -> {
                    result.setProfilePosts(
                            profilePosts.stream()
                                    .filter(profilePostDto -> profilePostDto.getPostId().equals(result.getPostId()))
                                    .collect(Collectors.toList()));
                    result.setTags(
                            tagList.stream()
                                    .filter(tagDto -> tagDto.getPostId().equals(result.getPostId()))
                                    .map(tagDto -> tagDto.getTagName())
                                    .collect(Collectors.toSet()));
                    result.setBadges(
                            memberBadges.stream()
                                    .filter(memberBadge -> memberBadge.getMemberId().equals(result.getMemberId()))
                                    .map(memberBadge -> memberBadge.getBadgeName())
                                    .collect(Collectors.toList()));
                    result.setImages(photos.stream()
                                    .filter(photo -> photo.getPost().getId().equals(result.getPostId()))
                            .map(photo -> photo.getStoredFileName())
                            .collect(Collectors.toList()));
                });

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchOne());
    }

    //마이페이지 작성글 보기
    @Override
    public Page<MyPostDto> findListByMemberId(Long memberId, MyPageCondition condition, Pageable pageable) {
        log.info("condition {} {}", condition.getAfter(), condition.getCategoryId());
        List<MyPostDto> results = queryFactory.select(new QMyPostDto(
                        post.id,
                        post.image,
                        post.title,
                        post.memberPostLikes.size(),
                        post.replies.size(),
                        post.viewCount
                )).from(post).distinct()
                .innerJoin(profilePost).on(post.id.eq(profilePost.post.id))
                .innerJoin(profile).on(profilePost.profile.id.eq(profile.id))
                .innerJoin(postSpecify1).on(post.id.eq(postSpecify1.post.id))
                .innerJoin(specify1).on(postSpecify1.specify1.id.eq(specify1.id))
                .leftJoin(member).on(profile.member.id.eq(member.id))
                .where(
                        member.id.eq(memberId),
                        ltPostId(condition.getAfter()),
                        eqSpecify1(condition.getCategoryId()),
                        profile.isDeleted.isFalse()
                )
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(post.countDistinct()).from(post)
                .innerJoin(profilePost).on(post.id.eq(profilePost.post.id))
                .innerJoin(profile).on(profilePost.profile.id.eq(profile.id))
                .innerJoin(postSpecify1).on(post.id.eq(postSpecify1.post.id))
                .innerJoin(specify1).on(postSpecify1.specify1.id.eq(specify1.id))
                .leftJoin(member).on(profile.member.id.eq(member.id))
                .where(
                        member.id.eq(memberId),
                        ltPostId(condition.getAfter()),
                        eqSpecify1(condition.getCategoryId()),
                        profile.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchOne());
    }

    //태그검색
    private BooleanBuilder likeTags(List<String> tags) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(!CollectionUtils.isEmpty(tags)){
            for (String tag : tags) {
                booleanBuilder.or(orTags(tag));
            }
        }
        return booleanBuilder;
    }

    //isFriends T,F 친구 피드 보기
    private BooleanBuilder eqFollowedFriends(Boolean isFriend, List<Long> followedProfileIds) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (isFriend && !CollectionUtils.isEmpty(followedProfileIds)) {
            for (Long followedProfileId : followedProfileIds) {
                booleanBuilder.or(orFollowedFriends(followedProfileId));
            }
        }
        return booleanBuilder;
    }

    //postId를 통해 인덱싱
    private BooleanExpression ltPostId(Long postId) {
        log.info("post Id is {}", postId);
        return postId == null ? null : post.id.lt(postId);
    }

    //생물종류1
    private BooleanExpression eqSpecify1(Long specify1Id) {
        return specify1Id == null ? null : postSpecify1.specify1.id.eq(specify1Id);
    }

    //tags
    private BooleanExpression orTags(String searchTag) {
        return StringUtils.isEmpty(searchTag) ? null : tag1.tag.like(searchTag);
    }


    //친구 피드 목록
    private BooleanExpression orFollowedFriends(Long followedProfileId) {
        return followedProfileId == null ? null : profilePost.profile.id.eq(followedProfileId);
    }

    //우선 title, content 비교
    private BooleanExpression searchCondition(String searchCondition, String searchKeyword) {
        try {
            BooleanExpression searchConditionExpression = null;
            if (searchCondition.equals("all")) {
                searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : post.title.contains(searchKeyword).or(post.content.contains(searchKeyword));
            } else if (searchCondition.equals("title")) {
                searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : post.title.contains(searchKeyword);
            } else if (searchCondition.equals("content")) {
                searchConditionExpression = StringUtils.isEmpty(searchCondition) ? null : post.content.contains(searchKeyword);
            }
            return searchConditionExpression;
        }
        catch (Exception e){
            return null;
        }
    }

    //프로필 정보
    public List<ProfilePostDto> profilePosts(List<Long> postIds) {
        List<ProfilePostDto> profilePosts = queryFactory
                .select(new QProfilePostDto(
                        post.id,
                        profilePost.profileName,
                        profile.image,
                        profile.isDeleted
                ))
                .from(profilePost).distinct()
                .innerJoin(profile).on(profile.id.eq(profilePost.profile.id))
                .innerJoin(post).on(post.id.eq(profilePost.post.id))
                .where(inProfilePostIds(postIds))
                .fetch();

        profilePosts.stream()
                .forEach(result -> {
                    if (result.isDeleted()) {
                        result.setImage("");
                    }
                });

        return profilePosts;
    }


    //profile post id batch size
    private BooleanExpression inProfilePostIds(List<Long> postIds) {
        return postIds.size() == 0 ? null : profilePost.post.id.in(postIds);
    }

    //postTag post id batch size
    private BooleanExpression inPostTagPostIds(List<Long> postIds) {
        return postIds.size() == 0 ? null : postTag.post.id.in(postIds);
    }
**/
}
