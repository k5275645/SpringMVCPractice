package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M08110 : 영업예상판매량
 * 작성일자 :: 2021.05.24
 * 작 성 자 :: 김태환
 */
@Repository
public interface M08110DaoMapper {
	
	/**
	 * 상단 sum 그리드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSum(Map<String, Object> params) throws Exception;
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * version Combo List 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> versionCombo(Map<String, Object> params) throws Exception;
	
	/**
	 * 메뉴 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> checkExcelList(Map<String, Object> params) throws Exception;
	
	/**
	 * 해당버전데이터 삭제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteVersionData(Map<String, Object> params) throws Exception;

	/**
	 * 해당버전데이터(월별) upload
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int uploadMonthVersionData(Map<String, Object> params) throws Exception;

	/**
	 * 해당버전데이터(월별 데이터 주차별 배부) upload
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int uploadMonthAllocWeekVersionData(Map<String, Object> params) throws Exception;

	/**
	 * 해당버전데이터(주차별) upload
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int uploadWeekVersionData(Map<String, Object> params) throws Exception;

	/**
	 * 신규버전 코드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getInpDgrSeq(Map<String, Object> params) throws Exception;
	
}
