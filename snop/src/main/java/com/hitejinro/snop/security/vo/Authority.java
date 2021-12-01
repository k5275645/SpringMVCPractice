package com.hitejinro.snop.security.vo;

import org.springframework.security.core.GrantedAuthority;

/**
 * 권한 객체
 * @author 남동희
 *
 */
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = -3227795758570486718L;

	private String role;

	public Authority() {
	}

	public Authority(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return this.role;
	}

	public void setAuthority(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Autority [role=" + role + "]";
	}

}
