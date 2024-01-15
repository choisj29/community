package com.example.community.service.member;

import com.example.community.domain.member.Member;
import com.example.community.repository.member.MemberRepository;
import com.example.community.service.file.FileStore;
import com.example.community.web.dto.file.UploadFileDto;
import com.example.community.web.dto.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 기능
     * 화면에서 JoinRequest(loginId, password, name)을 입력받아 Member로 변환 후 저장
     * loginId, name 중복 체크는 controller 진행 => 에러 메세지 출력을 위해
     */
    @Transactional
    public Member join(JoinMemberRequest dto) {
        return memberRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
    }

    //아이디 중복검사
    public boolean checkLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    //닉네임 중복검사
    public boolean checkNameDuplicate(String name) {
        return memberRepository.existsByName(name);
    }

    //회원조회 idx
    public Member findById(Long memberId) {
        //.get()의 경우 결과값이 null일 경우 NoSuchElementException 발생
        //Optional 반환하는 경우 원하는 값이 있으면 원하는 객체로 받고 없으면 Exception 처리를 하는 패턴을 사용
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member is null"));
    }

    public Optional<Member> findByLoginId(String loginId){
        log.info("findByLoginId {}",loginId);
        return memberRepository.findByLoginId(loginId);
    }

    /**
     * 회원탈퇴 기능
     * 탈퇴 시 연관 post, reply, item, rankLog 모두 cascade 삭제
     * logout
     * 화면에서 PasswordDto(password)을 입력 받아 본인 계정 확인
     * password 체크는 controller 진행 => 에러 메세지 출력을 위해
     */
    @Transactional
    public void withdrawal(Member member) {
        memberRepository.delete(member);
    }

}
