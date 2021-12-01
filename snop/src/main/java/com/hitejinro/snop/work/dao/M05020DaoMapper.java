package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M05020 : 선도관리 현황
 * 작성일자 :: 2021.8.04
 * 작 성 자 :: 김태환
 */
@Repository
public interface M05020DaoMapper {
	
	/**
	 * 가변날짜조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearMonthGrid(Map<String, Object> params) throws Exception;
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 차수 Combo List 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> yyyywwCombo(Map<String, Object> params) throws Exception;
	
	/**
	 * 조직(센터/공장) Combo List 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> centerCodeCombo(Map<String, Object> params) throws Exception;
	
	/**
	 * 팝업조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPop(Map<String, Object> params) throws Exception;
	
}
