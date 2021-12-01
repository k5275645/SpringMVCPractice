package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 재고 조회 > 전사 제품 적재 현황
 * @author 김남현
 *
 */
@Repository
public interface M04020DaoMapper {
	
	/**
	 * 전사 제품 적재 현황 > QC, 수출주 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal }
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchExpQcStock(Map<String, Object> params) throws Exception;
	
	/**
	 * 전사 제품 적재 현황 > 기간 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal }
	 * @return { MIN_YYYYMMDD, MAX_YYYYMMDD}
	 * @throws Exception
	 */
	public Map<String, Object> searchPeriod(Map<String, Object> params) throws Exception;
	
	/**
	 * 전사 제품 적재 현황 > 차트 데이터 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal, MIN_YYYYMMDD, MAX_YYYYMMDD }
	 * @return [{ STD_PERIOD, TOTAL_QTY, QC_QTY, EXP_QTY, WH_LOAD_RATE, OPNSTOR_LOAD_RATE }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchChart(Map<String, Object> parmas) throws Exception;
	
	/**
	 * 전사 제품 적재 현황 > 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal, MIN_YYYYMMDD, MAX_YYYYMMDD }
	 * @return [{ ORG_CODE, ORG_NAME, A.ORG_OPT_CAPA, A.ORG_OPT_PT_CAPA, A.ORG_MAX_CAPA, A.ORG_MAX_PT_CAPA.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 전사 제품 적재 현황 > 저장
	 * @param params { ORG_CODE, RMKS..., yyyymmdd, unitVal, capaVal, userId }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
}
