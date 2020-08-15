package com.ashago.mainapp.interceptor;

import com.ashago.mainapp.resp.RespField;
import com.ashago.mainapp.util.RequestThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case RespField
                            .SESSION_ID:
                        RequestThreadLocal.setSessionId(cookie.getValue());
                    default:
                        //do nothing
                }
            }
        }
        return true;
    }
}
