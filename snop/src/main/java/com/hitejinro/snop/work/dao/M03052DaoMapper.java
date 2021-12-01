package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 용기 지표 구매폐기
 * @author 남동희
 *
 */
@Repository
public interface M03052DaoMapper {
	
	/**
	 * 연간 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearly(Map<String, Object> params) throws Exception;
	
	/**
	 * 월간 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMonthly(Map<String, Object> params) throws Exception;

}
