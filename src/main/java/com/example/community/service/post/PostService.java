package com.example.community.service.post;

import com.example.community.domain.member.Member;
import com.example.community.domain.memberPostLike.MemberPostLike;
import com.example.community.domain.post.Post;
import com.example.community.repository.post.PostRepository;
import com.example.community.service.file.FileStore;
import com.example.community.service.member.MemberService;
import com.example.community.service.memberPostLike.MemberPostLikeService;
import com.example.community.web.dto.file.UploadFileDto;
import com.example.community.web.dto.post.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.time.LocalDateTime.now;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberPostLikeService memberPostLikeService;
    private final FileStore fileStore;
    //private final JdbcPostRepository jdbcPostRepository;
    private final MemberService memberService;

    //게시글 저장
    @Transactional
    public Long save(Post post) {
        return postRepository.save(post).getId();
    }

    @Transactional
    public Long savePost(SavePostRequest dto) throws IOException {
        Member member = memberService.findById(dto.getMemberId());
        Post post = postRepository.save(dto.toEntity());
        post.setMember(member);

        //첨부파일
        if (!CollectionUtils.isEmpty(dto.getImages())) {
            for (MultipartFile image : dto.getImages()) {
                UploadFileDto imageFile = fileStore.storeFile(image);
            }
        }

        return post.getId();
    }


    //태그저장
    @Transactional
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post is null"));
    }

    //게시글 목록
//    public Page<ListPostDto> searchNoOffset(PostCondition postCondition, Pageable pageable) {
//        return postRepository.searchNoOffset(postCondition, pageable);
//    }

    //게시글 수정
    @Transactional
    public PostDto modifyPost(Long postId, ModifyPostDto dto) {
        Post post = this.findById(postId);
        Member member = memberService.findById(dto.getMemberId());
        if(post.getMember().getId().equals(member.getId())){ //본인글 확인
            //dto.getContent().replace("\n","<br>");
            post.update(dto);
        }
        return new PostDto(post);
    }

    //글삭제
    @Transactional
    public void deletePost(Long postId) {
        postRepository.delete(findById(postId));
    }

    //좋아요
    @Transactional
    public String likePost(Post post, Member member) {

        MemberPostLike memberPostLike = memberPostLikeService.findByPostAndMember(post, member);
        if (memberPostLike == null ) {
            MemberPostLike mpl = MemberPostLike.createMemberPostLike(member, post);
            memberPostLikeService.save(mpl);
            return "좋아요 등록";
        } else {
            memberPostLikeService.delete(memberPostLike);
            return "좋아요 취소";
        }
    }

    public Page<ListPostDto> search(SearchReq searchReq, Pageable pageable) {
        return postRepository.search(searchReq, pageable);
    }

    //mypage postList
//    public Page<MyPostDto> findListByMemberId(Long memberId, MyPageCondition condition, Pageable pageable) {
//        return postRepository.findListByMemberId(memberId, condition, pageable);
//    }

}
