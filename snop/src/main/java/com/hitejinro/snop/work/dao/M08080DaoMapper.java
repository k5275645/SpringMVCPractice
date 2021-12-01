package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > 제품 과다/부족 기준
 * @author 김남현
 *
 */
@Repository
public interface M08080DaoMapper {
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 조회
	 * @param params { yyyymm, liquorCd }
	 * @return { [{ITEM_IGRD_TYPE_CODE, STOCK_STATS_CODE, SEQ...}] }
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 삭제
	 * @param params { PERIOD_YYYYMM, LIQUOR_CODE, ITEM_IGRD_TYPE_CODE, STOCK_STATS_CODE, SEQ }
	 * @return 
	 * @throws Exception
	 */
	public int delete(Map<String, Object> parmas) throws Exception;
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 수정
	 * @param params { PERIOD_YYYYMM, LIQUOR_CODE, ITEM_IGRD_TYPE_CODE, STOCK_STATS_CODE, SEQ, ...RMKS }
	 * @return 
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 버전콤보조회
	 * @param params {}
	 * @return { [{CODE, NAME}] }
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVerList(Map<String, Object> params) throws Exception;
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 버전복사
	 * @param params { yyyymm, fromVer }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	public int deleteNowVer(Map<String, Object> params) throws Exception;
	public int copyLastVer(Map<String, Object> params) throws Exception;
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 팝업창 검색
	 * @param params { searchWord, yyyymm, liquorCd }
	 * @return [{ ITEM_CODE, DESCRIPTION }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPop(Map<String, Object> params) throws Exception;
	
}
