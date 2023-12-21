package com.example.community.web.interceptor;


import com.example.community.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = getFullURL(request);
        log.info("LoginCheckInterceptor 실행 {}", requestURI);

        HttpSession session = request.getSession();
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            log.info("로그인 안한 회원의 요청");
            //로그인으로 리다이렉트
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        return true;
    }

    public String getFullURL(HttpServletRequest request){
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if(queryString == null){
            return requestURL.toString();
        }else{
            return requestURL.append('?').append(queryString).toString();
        }

    }
}
