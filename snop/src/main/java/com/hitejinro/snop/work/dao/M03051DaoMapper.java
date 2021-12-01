package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 공병 생산 지표
 * @author 남동희
 *
 */
@Repository
public interface M03051DaoMapper {
	
	/**
	 * 연간 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, acctType, liquorCode, mfgCode, volumeValue, brandCode }
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, VOLUME_VALUE, BRAND_CODE, MFG_CODE, NEW_BOTL_RATE, PRDT_QTY...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearly(Map<String, Object> params) throws Exception;
	
	/**
	 * 월간 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, acctType, liquorCode, mfgCode, volumeValue, brandCode }
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, VOLUME_VALUE, BRAND_CODE, MFG_CODE, NEW_BOTL_RATE, PRDT_QTY...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMonthly(Map<String, Object> params) throws Exception;
	
	/**
	 * 일간 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, acctType, liquorCode, mfgCode, volumeValue, brandCode }
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, VOLUME_VALUE, BRAND_CODE, MFG_CODE, NEW_BOTL_RATE, PRDT_QTY...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDaily(Map<String, Object> params) throws Exception;
}
