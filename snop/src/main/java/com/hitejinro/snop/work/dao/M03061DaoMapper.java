package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제병사실적 관리(조회)
 * @author 남동희
 *
 */
@Repository
public interface M03061DaoMapper {
	
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
	
	/**
	 * 연간 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, liquorCode, acctType, volumeValue, itemCode, headerList : [{CODE, NAME]}
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, ITEM_CODE, VOLUME_VALUE, QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearly(Map<String, Object> params) throws Exception;

	/**
	 * 월간 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, liquorCode, acctType, volumeValue, itemCode, headerList : [{CODE, NAME]}
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, ITEM_CODE, VOLUME_VALUE, QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMonthly(Map<String, Object> params) throws Exception;

	/**
	 * 일간 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, liquorCode, acctType, volumeValue, itemCode, headerList : [{CODE, NAME]}
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, ITEM_CODE, VOLUME_VALUE, QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDaily(Map<String, Object> params) throws Exception;

}
