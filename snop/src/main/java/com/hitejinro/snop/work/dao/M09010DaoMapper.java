package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 생산계획 진척률
 * @author 남동희
 *
 */
@Repository
public interface M09010DaoMapper {

	/**
	 * 헤더 조회
	 * @param params {startDate, endDate} 
	 * @return [{PERIOD_YYYYMMDD, HEADER1_DESC...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;

	/**
	 * 조회
	 * @param params { startDate, endDate, liquorCode, mfgCode, acctType, unit, segment2, segment3, brandCode, usageCode, vesselCode, volumeValue, domExpCode, mainFlag, eventItemFlag }
	 * @return 
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchGrid(Map<String, Object> params) throws Exception;

	public List<Map<String, Object>> searchWeeklyChart(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> searchMonthlyChart(Map<String, Object> params) throws Exception;

}
