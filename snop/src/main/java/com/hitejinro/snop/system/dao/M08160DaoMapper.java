package com.hitejinro.snop.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 메뉴 관리
 * @author 남동희
 *
 */
@Repository
public interface M08160DaoMapper {

	/**
	 * 메뉴 조회
	 * @param params {}
	 * @return { Body : [[{MENU_CD, MENU_NM, MENU_DESC...}]]}
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

	/**
	 * 유효성 검증
	 * @param params { updateList, userId }
	 * @return [{MENU_CD, MENU_NM, IDX번째 행 오류...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> validate(Map<String, Object> params) throws Exception;

	/**
	 * 추가/수정
	 * @param params { updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	/**
	 * 상위메뉴N -> 하위메뉴N
	 * @param params { updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int udpateDetailUseYn(Map<String, Object> params) throws Exception;

	/**
	 * 메뉴 사용N -> 권한-메뉴 삭제
	 * @param params { updateList, employeeNumber }
	 * @return
	 * @throws Exception
	 */
	public int deleteAuthMenu(Map<String, Object> params) throws Exception;

}
