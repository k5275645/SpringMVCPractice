package com.hitejinro.snop.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.hitejinro.snop.common.util.SessionUtil;

/**
 * 로그인 성공시 처리
 * @author 남동희
 *
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	    
        // - User Log 기록
	    SessionUtil sessionUtil = new SessionUtil(); // - Inject/Autowired방식이 아닌 직접 생성자를 호출하는 방식 사용 : Inject/Autowired 방식 사용시, SessionUtil을 못찾아서 오류가 발생
        sessionUtil.insertUserLog(userDetailsService, request);
	    
	    // 메인 화면으로 이동
		response.sendRedirect(request.getContextPath() + "/");
	}

}
