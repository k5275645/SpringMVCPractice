package com.hitejinro.snop.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hitejinro.snop.security.dao.SecurityDaoMapper;
import com.hitejinro.snop.security.vo.Authority;
import com.hitejinro.snop.security.vo.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Inject
    SecurityDaoMapper securityDaoMapper;

    @Override
    public User loadUserByUsername(String employeeNumber) throws UsernameNotFoundException, BadCredentialsException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("employeeNumber", employeeNumber);

        // 1. 사용자 정보 조회
        User user = securityDaoMapper.loadUserByUsername(params);

        if (user == null || StringUtils.isEmpty(user.getUsername())) {
            throw new UsernameNotFoundException("유효하지 않은 사용자 정보");
        }
        
        // 2. Spring 권한 체크
        if(StringUtils.isEmpty(user.getAuthCd()) || StringUtils.isEmpty(user.getRoleCd()) || "ROLE_ANONYMOUS".equals(user.getRoleCd())) {
            throw new BadCredentialsException("접속 권한이 존재하지 않는 사용자");
        }

        // 3. Spring 권한 설정
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new Authority(user.getRoleCd()));
        user.setAuthorities(authorities);
        
        // 4. 사용자의 메뉴-권한 정보 설정
        List<Map<String, Object>> arrAuthMenuList = securityDaoMapper.selectUserAuthMenuList(params);
        user.setAuthMenuList(arrAuthMenuList);

        return user;
    }
    
    /**
     * 사용자의 메뉴-권한 정보 조회
     * @param params {employeeNumber : 사번}
     * @return [{ USER_ID, USER_NM, AUTH_CD, AUTH_NM, MENU_CD, MENU_NM, URL, ALOW_YN_BTN_SELECT, ALOW_YN_BTN_SAVE, ALOW_YN_BTN_EXEC }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectUserAuthMenuList(Map<String, Object> params) throws Exception {
        return securityDaoMapper.selectUserAuthMenuList(params);
    }
    
    /**
     * 사용자로그 생성 : ID/PWD 로그인을 제외하고 CommonInterceptor 에서 사용
     * @param params { serverMode, serverUrl, requestUrl, clientIp, menuUrl, parameter, userId }
     * @return 
     * @throws Exception
     */
    public int insertUserLog(Map<String,Object> params) throws Exception {
        int i = 0;
        try {
            i = securityDaoMapper.insertUserLog(params);
        } catch(Exception e) {
            throw e;
        } finally {
        }
        return i;
    }

}
