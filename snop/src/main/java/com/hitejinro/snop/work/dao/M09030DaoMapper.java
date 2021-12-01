package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 반제품 생산실적
 * @author 남동희
 *
 */
@Repository
public interface M09030DaoMapper {
	
	/**
	 * 헤더 조회
	 * @param params {YYYYMM, startDate, endDate}
	 * @return [{NAME, VISIBLE, DEF, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
	
	/**
	 * 조회
	 * @param params {YYYYMM, startDate, endDate, liquorCode, mfgCode, itemCode, unit}
	 * @return [{MFG_CODE, ITEM_CODE, MFG_NAME, DESCRIPTION, SEGMENT3, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

}
