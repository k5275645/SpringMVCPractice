package com.hitejinro.snop.security.dao;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Repository;

import com.hitejinro.snop.security.vo.User;

@Repository
public interface SecurityDaoMapper {
    
    /**
     * 사용자 정보 조회
     * @param params {employeeNumber : 사번}
     * @return {EMPLOYEE_NUMBER, EMPLOYEE_KOR_NAME, CORPORATION_CODE, ORG_ID, ...}
     * @throws Exception
     */
    public User loadUserByUsername(Map<String, Object> params) throws AuthenticationException;
    
    /**
     * 사용자의 메뉴-권한 정보 조회
     * @param params {employeeNumber : 사번}
     * @return [{ USER_ID, USER_NM, AUTH_CD, AUTH_NM, MENU_CD, MENU_NM, URL, ALOW_YN_BTN_SELECT, ALOW_YN_BTN_SAVE, ALOW_YN_BTN_EXEC }]
     */
    public List<Map<String, Object>> selectUserAuthMenuList(Map<String, Object> params);

    /**
     * 사용자로그 생성
     * @param params { serverMode, serverUrl, requestUrl, clientIp, menuUrl, parameter, userId }
     * @return 
     * @throws Exception
     */
    public int insertUserLog(Map<String, Object> params) throws Exception;

}
