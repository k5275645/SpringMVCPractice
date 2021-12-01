package com.hitejinro.snop.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;

/**
 * 예외를 일괄적으로 처리
 * @author 남동희
 *
 */
@RestControllerAdvice
public class ExceptionController {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	/**
	 * 비지니스 로직에서 사용자 정의 예외
	 * @param e the exception
	 * @return ResponseEntity {Head : {}, Body : {_RESULT_FLAG : 'ERROR', _RESULT_MSG : 'e.getMessage()'}
	 * @throws Exception
	 */
	@ExceptionHandler(UserException.class)
	public ResponseEntity<Map<String, Object>> userException(UserException e) throws Exception {
		logger.error("userException", e);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.RESULT_FLAG, "E");
		result.put(Const.RESULT_MSG, e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}

	/**
	 * 요청 method 오류
	 * @param e the exception
	 * @return ResponseEntity {Head : {}, Body : {_RESULT_FLAG : 'ERROR', _RESULT_MSG : '유효하지 않은 요청입니다.'}
	 * @throws Exception
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String, Object>> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) throws Exception {
		logger.error("httpRequestMethodNotSupportedException", e);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.RESULT_FLAG, "E");
		result.put(Const.RESULT_MSG, "유효하지 않은 요청입니다.");

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
	}

	/**
	 * 요청 parameter 오류
	 * @param e the exception
	 * @return ResponseEntity {Head : {}, Body : {_RESULT_FLAG : 'ERROR', _RESULT_MSG : '유효하지 않은 요청입니다.'}
	 * @throws Exception
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> httpMessageNotReadableException(HttpMessageNotReadableException e) throws Exception {
		logger.error("httpMessageNotReadableException", e);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.RESULT_FLAG, "E");
		result.put(Const.RESULT_MSG, "유효하지 않은 요청입니다.");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

	/**
	 * 응답 오류
	 * @param e the exception
	 * @return ResponseEntity {Head : {}, Body : {_RESULT_FLAG : 'ERROR', _RESULT_MSG : '유효하지 않은 요청입니다.'}
	 * @throws Exception
	 */
	@ExceptionHandler(HttpMessageNotWritableException.class)
	public ResponseEntity<Map<String, Object>> httpMessageNotWritableException(HttpMessageNotWritableException e) throws Exception {
		logger.error("httpMessageNotWritableException", e);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.RESULT_FLAG, "E");
		result.put(Const.RESULT_MSG, "유효하지 않은 응답입니다.");

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}

	/**
	 * 일반적인 예외
	 * @param e the exception
	 * @return ResponseEntity {Head : {}, Body : {_RESULT_FLAG : 'ERROR', _RESULT_MSG : '오류가 발생하였습니다.\n 관리자에게 문의하세요.'}
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> exception(Exception e) throws Exception {
		logger.error("Exception", e);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.RESULT_FLAG, "E");
		
        // - 로그인 관련 오류 처리
		if(e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
		    result.put(Const.RESULT_MSG, e.getMessage());
		} else {
	        result.put(Const.RESULT_MSG, "오류가 발생하였습니다.\n관리자에게 문의하세요.");
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
}
