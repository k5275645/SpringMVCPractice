package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제품수급 > 일일 품목별 PSI
 * @author 유기후
 *
 */
@Repository
public interface M02010DaoMapper {
    
    /**
     * 데이터 조회 : 품목별 수급 그리드
     * @param params { bssYYYYMMDD, itemGubun(품목별기준 : INTEREST_ITEM(주요품목), ALL_ITEM(전체품목)), itemMapYn, domExpType, stdSale(판매기준), maxSale(최대판매), acctCd(계정구분 : ACCT_UOM_CS_QTY(환산c/s), ACCT_CS_QTY(단순c/s)) }
     * @return [{ YYYYMMDD, ITEM_CODE, LIQUOR_DESC, VESSEL_DESC, ITEM_NAME, ITEM_CODE_NAME, MAIN_FLAG_DESC, STOCK_QTY, ACTUAL_SALE_QTY, ACTUAL_20D_SALE_AVG_QTY, ACTUAL_20D_SALE_AVG_QTY_STOCK_DAY, ACTUAL_5D_SALE_AVG_QTY, ACTUAL_5D_SALE_AVG_QTY_STOCK_DAY, ACTUAL_SEARCH_SALE_AVG_QTY, ACTUAL_SEARCH_SALE_AVG_QTY_STOCK_DAY, ESPN_SALE_AVG_QTY, ESPN_SALE_AVG_QTY_STOCK_DAY, SEARCH_MAX_SALE_WEEK_AVG_QTY, SEARCH_MAX_SALE_WEEK_AVG_QTY_STOCK_DAY, MIN_STOCK_DCNT, STRG_SAFT_STOCK_DCNT, STRG_SAFT_MAX_STOCK_DCNT, TRANS_QTY, PRDT_NORGSTD_QTY, STOCK_QC_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchItemSplDmd(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 재고현황 그리드(맥주/소주를 각각 조회)
     * @param params { bssYYYYMMDD, acctCd(계정구분 : ACCT_UOM_CS_QTY(환산c/s), ACCT_CS_QTY(단순c/s)), liquorCode }
     * @return [{ LIQUOR_CODE, GUBUN, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, GUBUN_RATIO, STOCK_QTY, STOCK_DAY, ACTUAL_20D_SALE_AVG_QTY, STOCK_CONV_QTY, ACTUAL_20D_SALE_AVG_CONV_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchStockCurst(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 공장 이벤트 그리드
     * @param params { bssYYYYMMDD }
     * @return [{ MFG_TXT, LINE_TXT, EVENT_TXT, MFG_EVENT_MNG_SEQNO, USE_YN, VLD_STR_DT, VLD_END_DT }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchMfgEvent(Map<String, Object> params) throws Exception;
    
    
    /**
     * 데이터 조회 : 재고현황 관리 그리드
     * @param params {  }
     * @return [{ ITEM_GROUP_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchStockCurstMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 재고현황 관리의 생성
     * @param params { userId, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }
     * @return
     * @throws Exception
     */
    public int insertStockCurstMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 재고현황 관리의 수정
     * @param params { userId, ITEM_GROUP_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }
     * @return
     * @throws Exception
     */
    public int updateStockCurstMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 재고현황 관리의 삭제
     * @param params { userId, ITEM_GROUP_MNG_SEQNO }
     * @return
     * @throws Exception
     */
    public int deleteStockCurstMng(Map<String, Object> params) throws Exception;
    
    
    /**
     * 데이터 조회 : 공장 이벤트 관리 그리드
     * @param params { bssYYYY }
     * @return [{ MFG_EVENT_MNG_SEQNO, MFG_TXT, LINE_TXT, EVENT_TXT, USE_YN, VLD_STR_DT, VLD_END_DT }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchMfgEventMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 공장 이벤트 관리의 생성
     * @param params { userId, MFG_TXT, LINE_TXT, EVENT_TXT, USE_YN, VLD_STR_DT, VLD_END_DT }
     * @return
     * @throws Exception
     */
    public int insertMfgEventMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 공장 이벤트 관리의 수정
     * @param params { userId, MFG_EVENT_MNG_SEQNO, MFG_TXT, LINE_TXT, EVENT_TXT, USE_YN, VLD_STR_DT, VLD_END_DT }
     * @return
     * @throws Exception
     */
    public int updateMfgEventMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 공장 이벤트 관리의 삭제
     * @param params { userId, MFG_EVENT_MNG_SEQNO }
     * @return
     * @throws Exception
     */
    public int deleteMfgEventMng(Map<String, Object> params) throws Exception;
    
}
