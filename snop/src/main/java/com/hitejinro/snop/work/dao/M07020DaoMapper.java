package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M07020 :  주간 판매계획 준수율
 * 작성일자 :: 2021.09.07
 * 작 성 자 :: 김태환
 */
@Repository
public interface M07020DaoMapper {
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 최종 갱신일자 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchUpdtmStr(Map<String, Object> params) throws Exception;
	
	/**
	 * 상세 그리드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSubGrid(Map<String, Object> params) throws Exception;
	
	/**
	 * 월별 Chart 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchChart(Map<String, Object> params) throws Exception;
	
	/**
	 * 주차별 Chart 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchWeekChart(Map<String, Object> params) throws Exception;
	
	/**
	 * yyyyww Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> yyyywwCombo(Map<String, Object> params) throws Exception;
	
}
