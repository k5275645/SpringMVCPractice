package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제병사실적 관리(입력)
 * @author 남동희
 *
 */
@Repository
public interface M03060DaoMapper {

	/**
	 * 조회
	 * @param params {startDate, endDate, botlManursCode, liquorCode, itemCode}
	 * @return [[{PERIOD_YYYYMMDD, BOTL_MANURS_CODE, LIQUOR_CODE, ITEM_CODE, BOTL_PACKING_TYPE, ...}]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 저장
	 * @param params [{action, PERIOD_YYYYMMDD, BOTL_MANURS_CODE, LIQUOR_CODE, ITEM_CODE, BOTL_PACKING_TYPE, ...}]
	 * @return count
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
}
