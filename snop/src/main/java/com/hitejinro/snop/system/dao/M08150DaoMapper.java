package com.hitejinro.snop.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 권한 관리
 * @author 김남현
 *
 */
@Repository
public interface M08150DaoMapper {

	/**
	 * 권한 조회
	 * @param params { useYN }
	 * @return [[{AUTH_CD, AUTH_NM, AUTH_DESC...}]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

	/**
	 * 권한 유효성 검증
	 * @param params{ updateList, userId }
	 * @return [{AUTH_CD, IDX번째 행 오류...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> validate(Map<String, Object> updateData) throws Exception;
	
	/**
	 * 권한 추가/수정
	 * @param params{ updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> updateData) throws Exception;
	
	/**
	 * 권한/사용자 mapping 삭제
	 * @param updateData { updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int deleteAuthUser(Map<String, Object> updateData) throws Exception;
	
	/**
	 * 권한/메뉴 mapping 삭제
	 * @param updateData { updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int deleteAuthMenu(Map<String, Object> updateData) throws Exception;
	
	/**
	 * 권한콤보 조회
	 * @param params { authCd, authNm }
	 * @return [[{ AUTH_CD, AUTH_NM }]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAuth(Map<String, Object> params) throws Exception;
	
	/**
	 * 권한/메뉴 조회
	 * @param params { authCd }
	 * @return [[{MENU_CD, MENU_NM, MENU_DESC...}]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetail(Map<String, Object> params) throws Exception;
	
	/**
	 * 권한/메뉴 삭제
	 * @param params { updateList, authCd, userId }
	 * @return 
	 * @throws Exception
	 */
	public int deleteDetail(Map<String, Object> params) throws Exception;
	
	/**
	 * 권한/메뉴 추가/수정
	 * @param params { updateList, authCd, userId }
	 * @return 
	 * @throws Exception
	 */
	public int updateDetail(Map<String, Object> params) throws Exception;
	
}
