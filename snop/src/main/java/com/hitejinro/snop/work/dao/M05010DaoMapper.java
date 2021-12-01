package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M05010 : 체화재고 현황
 * 작성일자 :: 2021.7.27
 * 작 성 자 :: 김태환
 */
@Repository
public interface M05010DaoMapper {
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 상단 요약정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> summaryResult(Map<String, Object> params) throws Exception;
	
	/**
	 * 차수 Combo List 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> yyyywwCombo(Map<String, Object> params) throws Exception;
	
	/**
	 * 지점 Combo List 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> deptCodeCombo(Map<String, Object> params) throws Exception;
	
	/**
	 * 센터 Combo List 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> centerCodeCombo(Map<String, Object> params) throws Exception;
	
	/**
	 * 년, 월, 주차 기본설정(최종입력주차) 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> defaultYearMonth(Map<String, Object> params) throws Exception;
	
	/**
	 * 마감 기준일자 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMagamStr(Map<String, Object> params) throws Exception;
	
}
