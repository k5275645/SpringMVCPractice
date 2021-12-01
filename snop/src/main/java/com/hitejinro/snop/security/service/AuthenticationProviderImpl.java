package com.hitejinro.snop.security.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hitejinro.snop.security.vo.User;

/**
 * 인증 절차 수행
 * @author 남동희
 *
 */
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderImpl.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserDetailsServiceImpl userDetailServiceImpl;

	/**
	 * 1. 인증 정보 확인
	 * 2. UserDetailService.loadUserByName()을 통해 사용자 정보 추출
	 * 3. 비밀번호 체크
	 * 4. UsernamePasswordAuthenticationToken(user, password, authorities)를 통해 사용자 token 발급
	 * 
	 * @param authentication 인증 객체
	 * @return Authentication {principal, credentials, authorities}
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException, BadCredentialsException, UsernameNotFoundException {

		String employeeNumber = authentication.getName();
		String password = (String) authentication.getCredentials();

		// 1. 인증 정보 확인
		if (StringUtils.isEmpty(employeeNumber) || StringUtils.isEmpty(password)) {
			throw new BadCredentialsException("유효하지 않은 입력값");
		}

		// 2. 사용자 정보 추출
		User user = userDetailServiceImpl.loadUserByUsername(employeeNumber);

		// 3. 비밀번호, 계정 활성화 여부 체크
		// BCrypt 방식(ex : $2a$10$goek4HUPGO1Yw2JJBPiWiuzOo.GC1gBoXSSxb0gXrVkudCjvJpA4G)
		// $2a : version of Bcrypt
		// $10 : the strength
		// $goek4HUPGO1Yw2JJBPiWiuzOo : the salt
		// BCrypt matches -> 입력받은 password를 기존 salt를 활용하여 encode 후 비교
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("유효하지 않은 사용자 정보");
		}

		return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
	}

	/**
	 * 해당 AuthenticationProvider가 표시된 인증 객체를 지원하는 경우 true 반환
	 * @param authentication 인증 객체
	 * @return true -> 해당 인증 객체를 지원
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
