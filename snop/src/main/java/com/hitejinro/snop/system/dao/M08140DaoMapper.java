package com.hitejinro.snop.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 사용자 관리
 * @author 김남현
 *
 */
@Repository
public interface M08140DaoMapper {

	/**
	 * 사용자관리 조회
	 * @param params { searchType, searchWord, useYN }
	 * @return [{ EMPLOYEE_NUMBER, USER_NM, USER_ID, DEPT_CD, DEPT_NM, AUTH_CD, POSITION_CODE, POSITION_NAME, DUTY_CODE, DUTY_NAME, OFFICE_EMAIL, MOBILE_PHONE, USE_YN, AUTO_AUTH_LOCK_YN, AUTH_ORG_LEVEL, AUTH_ORG_CODE, AUTH_ADD_ORG_CODE_LIST, LAST_UPDATE_DATE, LAST_UPDATED_BY }]
	 * @throws Exception
	 */
	List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

    /**
     * 권한 리스트 조회
     * @param params {  }
     * @return [{ CODE, NAME }]
     * @throws Exception
     */
    List<Map<String, Object>> selectAuthList(Map<String, Object> params) throws Exception;

    /**
     * 조직(권역/지점) 리스트 조회
     * @param params {  }
     * @return [{ DEPARTMENT_CODE, DEPARTMENT_KOREAN_NAME }]
     * @throws Exception
     */
    List<Map<String, Object>> selectOrgList(Map<String, Object> params) throws Exception;

    
	/**
	 * 사용자관리 저장
	 * @param params { updateList, userId }
	 * @return 
	 * @throws Exception
	 */
	public int update(Map<String, Object> updateData) throws Exception;
	
	/**
	 * 권한/사용자 삭제
	 * @param params { updateList, userId }
	 * @return 
	 * @throws Exception
	 */
	public int deleteAuthUser(Map<String, Object> updateData) throws Exception;

	/**
	 * 권한/사용자 추가/수정
	 * @param params { updateList, userId}
	 * @return 
	 * @throws Exception
	 */
	public int updateAuthUser(Map<String, Object> updateData) throws Exception;

    
    /**
     * 데이터 저장 : 사용자의 추가 조직권한(권역/지점) 추가(전체 삭제 후 추가 방식)
     * @param params { USER_ID, AUTH_ADD_ORG_CODE_LIST, userId}
     * @return 
     * @throws Exception
     */
    public int insertUserAuthAddDept(Map<String, Object> updateData) throws Exception;

    /**
     * 데이터 저장 : 사용자의 추가 조직권한(권역/지점) 삭제(전체 삭제 후 추가 방식)
     * @param params { USER_ID, userId}
     * @return 
     * @throws Exception
     */
    public int deleteUserAuthAddDept(Map<String, Object> updateData) throws Exception;


}
