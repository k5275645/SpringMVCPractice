package com.hitejinro.snop.common.util;

/**
 * 사용자 정의 예외
 * @author 남동희
 *
 */
public class UserException extends Exception {

	private String result;

	private String Message;

	private static final long serialVersionUID = -8038521954879451562L;

	public UserException() {

	}

	public UserException(String message) {
		this.Message = message;
	}

	public UserException(String result, String message) {
		this.result = result;
		this.Message = message;
	}

	@Override
	public String getMessage() {
		return this.Message;
	}

	public String getResult() {
		return result;
	}

	@Override
	public String toString() {
		return "UserException [result=" + result + ", Message=" + Message + "]";
	}

}
