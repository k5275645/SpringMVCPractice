package com.hitejinro.snop.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 로그인 실패시 처리
 * @author 남동희
 *
 */
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		logger.info("exception : " + exception);
		

		request.setAttribute("errMsg",exception.getMessage()); // - 에러 메시지 넘기기
		request.getRequestDispatcher("/WEB-INF/views/security/login.jsp").forward(request, response);
	}

}
