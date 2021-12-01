package com.hitejinro.snop.security.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 사용자 정보 및 권한 정보에 따른 data object
 * @author 남동희
 *
 */
public class User implements UserDetails {

    private static final long serialVersionUID = 4876531267036232110L;

    
    private String USER_ID; // - 사용자ID : 기본적으로 사번(EMPLOYEE_NUMBER)과 동일
    private String USER_NM;
    private String PASSWORD;

    // - 부서 : 
    private String DEPT_CD;
    private String DEPT_NM;
    // - 직급
    private String POSITION_CODE;
    private String POSITION_NAME;
    // - 직책
    private String DUTY_CODE;
    private String DUTY_NAME;
    
    private String OFFICE_EMAIL;
    private String MOBILE_PHONE;
    private String USE_YN;
    // - 사번
    private String EMPLOYEE_NUMBER;
    // - 자동권한 막기 여부 : Y(자동권한 설정 막기), N(자동권한 설정 허용)
    private String AUTO_AUTH_LOCK_YN;
    // - 권한 코드
    private String AUTH_CD;
    // - Spring의 Role : "security-context.xml"의 ROLE_ADMIN, ROLE_USER 참고. ROLE_ANONYMOUS 는 시스템 접근 불가
    private String ROLE_CD;
    // - Spring의 권한 객체 : 여기선 Spring의 Role을 단건 넣을 예정
    private Collection<? extends GrantedAuthority> authorities;
    
    // - 메뉴 권한 정보 : [{ USER_ID, USER_NM, AUTH_CD, AUTH_NM, MENU_CD, MENU_NM, URL, ALOW_YN_BTN_SELECT, ALOW_YN_BTN_SAVE, ALOW_YN_BTN_EXEC }]
    private List<Map<String,Object>> AUTH_MENU_LIST;
    // - 메뉴의 버튼 권한을 ModelAndView에 넘길 형태(메뉴 권한 정보 설정때 자동 생성). 버튼 권한이 존재하는 것만 생성 : { 화면ID + "_ALLOW_SELECT" : "Y", 화면ID + "_ALLOW_SAVE" : "Y", 화면ID + "_ALLOW_EXEC" : "Y" }
    private Map<String, Object> USER_AUTH_MENU_BTN_LIST;
    

    @Override
    public String getUsername() {
        return this.EMPLOYEE_NUMBER;
    }

    @Override
    public String getPassword() {
        return this.PASSWORD;
    }

    public String getUserNm() {
        return this.USER_NM;
    }

    public String getDeptCd() {
        return this.DEPT_CD;
    }
    
    public String getDeptNm() {
        return this.DEPT_NM;
    }

    public String getPositionCode() {
        return this.POSITION_CODE;
    }

    public String getPositionName() {
        return this.POSITION_NAME;
    }

    public String getDutyCode() {
        return this.DUTY_CODE;
    }

    public String getDutyName() {
        return this.DUTY_NAME;
    }

    public String getOfficeEmail() {
        return this.OFFICE_EMAIL;
    }

    public String getMobilePhone() {
        return this.MOBILE_PHONE;
    }

    public String getUseYn() {
        return this.USE_YN;
    }

    public String getAutoAuthLockYn() {
        return this.AUTO_AUTH_LOCK_YN;
    }

    public String getAuthCd() {
        return this.AUTH_CD;
    }

    public String getRoleCd() {
        return this.ROLE_CD;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User [USER_ID=" + USER_ID + ", USER_NM=" + USER_NM + ", PASSWORD=" + PASSWORD + ", DEPT_CD=" + DEPT_CD + ", POSITION_CODE=" + POSITION_CODE + ", DUTY_CODE=" + DUTY_CODE
                + ", OFFICE_EMAIL=" + OFFICE_EMAIL + ", MOBILE_PHONE=" + MOBILE_PHONE + ", USE_YN=" + USE_YN + ", EMPLOYEE_NUMBER=" + EMPLOYEE_NUMBER + ", AUTO_AUTH_LOCK_YN=" + AUTO_AUTH_LOCK_YN
                + ", AUTH_CD=" + AUTH_CD + ", ROLE_CD=" + ROLE_CD + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((EMPLOYEE_NUMBER == null) ? 0 : EMPLOYEE_NUMBER.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (EMPLOYEE_NUMBER == null) {
            if (other.EMPLOYEE_NUMBER != null)
                return false;
        } else if (!EMPLOYEE_NUMBER.equals(other.EMPLOYEE_NUMBER))
            return false;
        return true;
    }

    /**
     * @return the authMenuList
     */
    public List<Map<String, Object>> getAuthMenuList() {
        return this.AUTH_MENU_LIST;
    }

    /**
     * @param authMenuList the authMenuList to set
     */
    public void setAuthMenuList(List<Map<String, Object>> authMenuList) {
        // - [{ USER_ID, USER_NM, AUTH_CD, AUTH_NM, MENU_CD, MENU_NM, URL, ALOW_YN_BTN_SELECT, ALOW_YN_BTN_SAVE, ALOW_YN_BTN_EXEC }]
        this.AUTH_MENU_LIST = authMenuList;
        
        // - 메뉴의 버튼 권한을 ModelAndView에 넘길 형태 생성 : { 화면ID + "_ALLOW_SELECT" : "Y", 화면ID + "_ALLOW_SAVE" : "Y", 화면ID + "_ALLOW_EXEC" : "Y" }
        Map<String, Object> mUserAuthMenuBtnList = new HashMap<String, Object>();
        if(authMenuList != null && authMenuList.size() > 0) {
            for(int i = 0 ; i < authMenuList.size() ; i++) {
                if("Y".equals((String)authMenuList.get(i).get("ALOW_YN_BTN_SELECT"))) {
                    mUserAuthMenuBtnList.put((String)authMenuList.get(i).get("MENU_CD") + "_ALLOW_SELECT", "Y");
                }
                if("Y".equals((String)authMenuList.get(i).get("ALOW_YN_BTN_SAVE"))) {
                    mUserAuthMenuBtnList.put((String)authMenuList.get(i).get("MENU_CD") + "_ALLOW_SAVE", "Y");
                }
                if("Y".equals((String)authMenuList.get(i).get("ALOW_YN_BTN_EXEC"))) {
                    mUserAuthMenuBtnList.put((String)authMenuList.get(i).get("MENU_CD") + "_ALLOW_EXEC", "Y");
                }
            }
        }
        this.USER_AUTH_MENU_BTN_LIST = mUserAuthMenuBtnList;
    }
    
    /**
     * 사용자의 메뉴 버튼 권한 리스트를 반환(Map형식)
     * <p>{ 화면ID + "_ALLOW_SELECT" : "Y", 화면ID + "_ALLOW_SAVE" : "Y", 화면ID + "_ALLOW_EXEC" : "Y" }
     * @return
     */
    public Map<String, Object> getUserAuthMenuBtnList() {
        return this.USER_AUTH_MENU_BTN_LIST;
    }
    

}
