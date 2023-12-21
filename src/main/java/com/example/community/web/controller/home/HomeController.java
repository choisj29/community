package com.example.community.web.controller.home;

import com.example.community.domain.member.Member;
import com.example.community.web.argumentresolver.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home 으로 이동
        if(loginMember == null){
            return "home";
        }
        //세션이 유지되면 로그인된 홈으로 이동
        model.addAttribute("member", loginMember);
        return "home";
    }
}
