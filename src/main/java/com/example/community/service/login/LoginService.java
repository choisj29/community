package com.example.community.service.login;

import com.example.community.domain.member.Member;
import com.example.community.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public Member login(String loginId, String password) {
        return memberService.findByLoginId(loginId)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .orElseThrow(NoSuchElementException::new);
    }


}
