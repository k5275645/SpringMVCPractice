package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 라인별 생산효율
 * @author 남동희
 *
 */
@Repository
public interface M09070DaoMapper {
	
	/**
	 * 헤더 조회
	 * @param params {YYYYMM, startDate, endDate}
	 * @return [{NAME, VISIBLE, DEF, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
	
	/**
	 * 조회
	 * @param params {YYYYMM, startDate, endDate, liquorCode, mfgCode, segment2, segment3, brandCode, usageCode, vesselCode, volumeValue, domExpCode}
	 * @return [{MFG_CODE, DEPT_CODE, BRAND_CODE, ITEM_CODE, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

}
