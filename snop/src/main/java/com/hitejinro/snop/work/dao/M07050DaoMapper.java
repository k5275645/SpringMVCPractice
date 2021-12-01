package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * KPI > 제품재고일수
 * @author 이수헌
 *
 */
@Repository
public interface M07050DaoMapper {
	
    /**
     * 데이터 조회 : 그리드 헤더 조회(기간 데이터)
     * @param params { fromYYYYMM, toYYYYMM }
     * @return [{ PERIOD_YYYYMM, PERIOD_MMWW, PERIOD_YYYYMMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, HEADER_COL_SPAN, HEADER_COL_SPAN2  }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 그리드 바디 조회
     * @param params { fromYYYYMM, toYYYYMM, liquorCode, usageCode, vesselCode, volumeValue, packingType, packingUnit, planYn, TREEGRID_HEADER[{ PERIOD_YYYYMM, PERIOD_MMWW, COL_ID }] }
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> searchBody(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 팝업 그리드 헤더 조회(기간 데이터)
     * @param params { fromYYYYMM, toYYYYMM }
     * @return [{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchPopHeader(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 팝업 그리드 바디 조회
     * @param params { fromYYYYMM, toYYYYMM, liquorCode, usageCode, vesselCode, volumeValue, packingType, packingUnit, planYn, TREEGRID_HEADER[{ PERIOD_YYYYMM, PERIOD_MMWW, COL_ID }] }
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> searchPopBody(Map<String, Object> params) throws Exception;
		
	/**
	 * 마감년월 조회
	 * @return
	 * @throws Exception
	 */
	public String getMagamYmd() throws Exception;
	
	/**
	 * 콤보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCombo(Map<String, Object> params) throws Exception;
	
}
