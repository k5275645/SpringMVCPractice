package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 생산관리 > 제조원가
 * @author 유기후
 *
 */
@Repository
public interface M09060DaoMapper {
    
    /**
     * 최신 마감년월 조회
     * @param params {  }
     * @return [{ MAX_YYYYMM }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectMaxPeriod(Map<String, Object> params) throws Exception;
    
    
    /**
     * 데이터 조회 : 그리드 헤더 조회(기간 데이터)
     * @param params { frYYYYMM, toYYYYMM }
     * @return [{ PERIOD_CODE, PERIOD_NAME, PERIOD_SEQ, FR_YYYYMM, TO_YYYYMM, ACCT_CD, ACCT_NM, ACCT_SEQ, DATA_INFO, COL_ID }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 그리드 바디 조회
     * @param params { frYYYYMM, toYYYYMM, liquorCode, orgCode, itemSegment2, itemSegment3, brandCode, usageCode, vesselCode, domExpCode, mainFlag, eventItemFlag, TREEGRID_HEADER[{ FR_YYYYMM, TO_YYYYMM, ACCT_CD, COL_ID }] }
     * @return [{ LIQUOR_CODE, ORG_CODE, ITEM_CODE, LIQUOR_NAME, ORG_NAME, BRAND_NAME, USAGE_NAME, VESSEL_NAME, VOLUME_VALUE, ITEM_NAME, COL_1, COL_2, COL_3, COL_4, COL_5, COL_6, ... }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchBody(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 차트 조회
     * @param params { toYYYYMM, liquorCode, orgCode, itemSegment2, itemSegment3, brandCode, usageCode, vesselCode, domExpCode, mainFlag, eventItemFlag }
     * @return [{ LIQUOR_CODE, LIQUOR_NAME, ORG_CODE, ORG_NAME, PRDT_CONV_QTY, CONV_UNIT_MANUFT_COST_AMT, MANUFT_COST_AMT, RAW_MAT_COST_AMT, LABOR_COST_AMT, MANUFT_ECST_AMT, RAW_MAT_COST_RATE, LABOR_COST_RATE, MANUFT_ECST_RATE, ORG_GUBUN_SEQ }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchChart(Map<String, Object> params) throws Exception;
    
    
    
}
