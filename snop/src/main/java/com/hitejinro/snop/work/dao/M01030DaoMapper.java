package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 판매실적분석(품목)
 * @author 이수헌
 *
 */
@Repository
public interface M01030DaoMapper {

	/**
	 * 헤더 조회
	 * @param params {startDate, endDate} 
	 * @return [{PERIOD_YYYYMMDD, HEADER1_DESC...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;

	/**
	 * 조회
	 * @param params {startDate, endDate, liquorCode, acctType, PRE_DAY_YYYYMMDD, PRE_WEEK_YYYYMMDD, PRE_MONTH_YYYYMMDD, PRE_YEAR_YYYYMMDD, CRITERIA_YYYYMMDD, RECENT_HHMMSS}
	 * @return [{LIQUOR_CODE, VESSEL_CODE, VOLUME_VALUE, PERIOD_YYYYMMDD, QTY ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchBody(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getTerritoryCombo(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getDepartmentCombo(Map<String, Object> params) throws Exception;

	public List<Map<String, Object>> getPartCombo(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getSalesrepCombo(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getAccountCombo(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getSegment2Combo(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getSegment3Combo(Map<String, Object> params) throws Exception;

}
