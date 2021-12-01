package com.hitejinro.snop.security.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.hitejinro.snop.common.util.Const;

/**
 * 1. 스프링 ExceptionTranslationFilter 호출
 * 2. 미인증 상태(session invalid)인 경우 AuthenticationEntryPoint를 호출
 * 3. 접근 불가(access denied)인 경우 AccessdeniedHandler를 호출
 * 
 * 일반 요청인 경우 로그인 페이지로 redirect(entry-point-ref에 등록)
 * Ajax 요청인 경우 JSON 형식의 401에러 반환 => fail callback에서 해당 에러감지 후 로그인 페이지로 redirect
 * @author 남동희
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

	@Override
	@SuppressWarnings("unchecked")
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

		String header = request.getHeader("X-Requested-With");

		if ("XMLHttpRequest".equals(header)) {

			OutputStream out = response.getOutputStream();
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.FORBIDDEN.value());

			Map<String, Object> result = new HashMap<String, Object>();
			result.put(Const.RESULT_FLAG, "E");
			result.put(Const.RESULT_MSG, "접근이 거부되었습니다.");

			JSONObject object = new JSONObject();
			object.putAll(result);
			out.write(object.toJSONString().getBytes(StandardCharsets.UTF_8.name()));
			out.flush();

		} else {
			request.getRequestDispatcher("/WEB-INF/views/security/login.jsp").forward(request, response);
		}

	}

}
