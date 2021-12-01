package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제품수급 > 계획대비 실적 차이 분석
 * @author 유기후
 *
 */
@Repository
public interface M02040DaoMapper {
    
    /**
     * 데이터 조회 : 그리드 헤더 조회(기간 데이터)
     * @param params { fromYYYYMM, toYYYYMM }
     * @return [{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC, MAX_ACTUAL_SALE_QTY_DT, MAX_ACTUAL_SALE_QTY_YYYYWW_YN }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 그리드 바디 조회
     * @param params { fromYYYYMM, toYYYYMM, liquorCode, usageCode, vesselCode, volumeValue, acctCd(ACCT_UOM_CS_QTY(환산c/s), ACCT_CS_QTY(단순c/s)), TREEGRID_HEADER[{ PERIOD_YYYYMM, PERIOD_MMWW, COL_ID }] }
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> searchBody(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 추가/수정
     * @param params { userId, updateList[{ PERIOD_YYYYMM, PERIOD_MMWW, LIQUOR_CODE, SALE_RSN, PRDT_RSN, CHANGE_SALE_YN, CHANGE_PRDT_YN }] }
     * @return
     * @throws Exception
     */
    public int update(Map<String, Object> params) throws Exception;
    
}
