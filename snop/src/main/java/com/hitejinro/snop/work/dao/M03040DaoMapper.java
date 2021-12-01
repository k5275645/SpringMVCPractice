package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 용기수급현황(월보)
 * @author 남동희
 *
 */
@Repository
public interface M03040DaoMapper {
	
	/**
	 * 공병 - 생산 연누계
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, TOTAL_PRDT_QTY, EXCLUDE_NEW_BOTL_QTY, INCLUDE_NEW_BOTL_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearlyPrdt(Map<String, Object> params) throws Exception;
	
	/**
	 * 공병 - 생산 직전 6개월
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, TOTAL_PRDT_QTY, EXCLUDE_NEW_BOTL_QTY, INCLUDE_NEW_BOTL_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMonthlyPrdt(Map<String, Object> params) throws Exception;
	
	/**
	 * 공병 - 생산 동기
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, TOTAL_PRDT_QTY, EXCLUDE_NEW_BOTL_QTY, INCLUDE_NEW_BOTL_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPeriodPrdt(Map<String, Object> params) throws Exception;
	
	/**
	 * 공병 - 회수 연누계
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, TOTAL_PRDT_QTY, EXCLUDE_NEW_BOTL_QTY, INCLUDE_NEW_BOTL_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearlyReturn(Map<String, Object> params) throws Exception;
	
	/**
	 * 공병 - 회수 직전 6개월
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, TOTAL_PRDT_QTY, EXCLUDE_NEW_BOTL_QTY, INCLUDE_NEW_BOTL_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMonthlyReturn(Map<String, Object> params) throws Exception;
	
	/**
	 * 공병 회수 동기
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, TOTAL_PRDT_QTY, EXCLUDE_NEW_BOTL_QTY, INCLUDE_NEW_BOTL_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPeriodReturn(Map<String, Object> params) throws Exception;
}
