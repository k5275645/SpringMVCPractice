package com.hitejinro.snop.security.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 필터 클래스
 * @author ykw
 *
 */
@Component
public class CommonFilter implements Filter {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(CommonFilter.class);

    
    /**
     * @param req
     * @param res
     * @param chain
     */
    @SuppressWarnings("unused")
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            // - Request의 Body를 복사 : CommonInterceptor 에서 파라메터 체크할 때, Body 내용이 필요하므로
            CachedBodyHttpServletRequest wrapper = new CachedBodyHttpServletRequest((HttpServletRequest)request);
            chain.doFilter(wrapper, res);
            
        } catch(Exception e) {
            chain.doFilter(req, res);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) {
    }
    
    @Override
    public void destroy() {
    }
}
