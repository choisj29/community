package com.example.community.web.controller.member;

import com.example.community.SessionConst;
import com.example.community.domain.member.Member;
import com.example.community.service.login.LoginService;
import com.example.community.service.member.MemberService;
import com.example.community.web.dto.member.JoinMemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") JoinMemberRequest dto){
        return "members/join";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("member") JoinMemberRequest dto,
                       BindingResult bindingResult,
                       HttpServletRequest request) throws IOException {

        // loginId 중복 체크
        if(memberService.checkLoginIdDuplicate(dto.getLoginId())) {
            bindingResult.addError(new FieldError("member", "loginId", dto.getLoginId(),false,null,null,"로그인 아이디가 중복됩니다."));
        }
        // 닉네임 중복 체크
        if(memberService.checkNameDuplicate(dto.getName())) {
            bindingResult.addError(new FieldError("member", "name", dto.getName(),false,null,null,"닉네임이 중복됩니다."));
        }
        // 빈 칸 체크
        if (bindingResult.hasErrors()) {
            return "members/join";
        }

        Member joined = memberService.join(dto);
        Member loginMember = loginService.login(joined.getLoginId(),dto.getPassword());
        //로그인 성공 처리
        //세션이 있으면 있는 생성 반환, 없다면 신규 세션을 생성 request.getSession(true)
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }
}
