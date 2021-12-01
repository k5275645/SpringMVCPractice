package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 용기 지표(용기현황 Index)
 * @author 남동희
 *
 */
@Repository
public interface M03050DaoMapper {
	
	/**
	 * 헤더 조회
	 * @param params {startYYYYMM, endYYYYMM, liquorCode, acctType, volume, itemCode, includeYN}
	 * @return [{HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, HEADER2_EXPAND...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
	
	/**
	 * 연간 조회
	 * @param params {startYYYYMM, endYYYYMM, liquorCode, acctType, volume, itemCode, header : [{CODE, NAME}]}
	 * @return [{LIQUOR_CODE, VESSEL_CODE, VOLUME_VALUE, PERIOD_YYYYMMDD, QTY ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearly(Map<String, Object> params) throws Exception;
	
	/**
	 * 월간 조회
	 * @param params {startYYYYMM, endYYYYMM, liquorCode, acctType, volume, itemCode, header : [{CODE, NAME}]}
	 * @return [{LIQUOR_CODE, VESSEL_CODE, VOLUME_VALUE, PERIOD_YYYYMMDD, QTY ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMonthly(Map<String, Object> params) throws Exception;
	
	/**
	 * 일간 조회
	 * @param params {startYYYYMM, endYYYYMM, liquorCode, acctType, volume, itemCode, header : [{CODE, NAME}]}
	 * @return [{LIQUOR_CODE, VESSEL_CODE, VOLUME_VALUE, PERIOD_YYYYMMDD, QTY ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDaily(Map<String, Object> params) throws Exception;
}
