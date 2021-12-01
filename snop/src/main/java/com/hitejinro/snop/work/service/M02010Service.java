package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.work.dao.M02010DaoMapper;

/**
 * 제품수급 > 일일 품목별 PSI
 * @author 유기후
 *
 */
@Service
public class M02010Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M02010Service.class);

    @Inject
    private M02010DaoMapper m02010DaoMapper;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 데이터 조회 : 품목별 수급 그리드, 재고현황 그리드(맥주/소주를 각각 조회), 공장 이벤트 그리드
     * @param params { bssYYYYMMDD, itemGubun(품목별기준 : INTEREST_ITEM(주요품목), ALL_ITEM(전체품목)), itemMapYn, domExpType, stdSale(판매기준), maxSale(최대판매), acctCd(계정구분 : ACCT_UOM_CS_QTY(환산c/s), ACCT_CS_QTY(단순c/s)) }
     * @return { TREEGRID_ITEM_SPL_DMD:[{}], TREEGRID_STOCK_CURST_10:[{}], TREEGRID_TREEGRID_STOCK_CURST_20ITEM_SPL_DMD:[{}], TREEGRID_MFG_EVENT:[{}] }
     * @throws Exception
     */
    public Map<String, Object> search(Map<String, Object> params) throws Exception {
        if(params == null) params = new HashMap<String, Object>();
        
        // 1. 품목별 수급 그리드 : [{ YYYYMMDD, ITEM_CODE, LIQUOR_DESC, VESSEL_DESC, ITEM_NAME, ITEM_CODE_NAME, MAIN_FLAG_DESC, STOCK_QTY, ACTUAL_SALE_QTY, ACTUAL_20D_SALE_AVG_QTY, ACTUAL_20D_SALE_AVG_QTY_STOCK_DAY, ACTUAL_5D_SALE_AVG_QTY, ACTUAL_5D_SALE_AVG_QTY_STOCK_DAY, ACTUAL_SEARCH_SALE_AVG_QTY, ACTUAL_SEARCH_SALE_AVG_QTY_STOCK_DAY, ESPN_SALE_AVG_QTY, ESPN_SALE_AVG_QTY_STOCK_DAY, SEARCH_MAX_SALE_WEEK_AVG_QTY, SEARCH_MAX_SALE_WEEK_AVG_QTY_STOCK_DAY, MIN_STOCK_DCNT, STRG_SAFT_STOCK_DCNT, STRG_SAFT_MAX_STOCK_DCNT, TRANS_QTY, PRDT_NORGSTD_QTY, STOCK_QC_QTY }]
        params.put("TREEGRID_ITEM_SPL_DMD", m02010DaoMapper.searchItemSplDmd(params));
        
        // 2.재고현황 그리드(맥주/소주를 각각 조회) : [{ LIQUOR_CODE, GUBUN, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, GUBUN_RATIO, STOCK_QTY, STOCK_DAY, ACTUAL_20D_SALE_AVG_QTY, STOCK_CONV_QTY, ACTUAL_20D_SALE_AVG_CONV_QTY }]
        // 2.1. 맥주
        params.put("liquorCode", "10");
        params.put("TREEGRID_STOCK_CURST_10", m02010DaoMapper.searchStockCurst(params));
        // 2.2. 소주
        params.put("liquorCode", "20");
        params.put("TREEGRID_STOCK_CURST_20", m02010DaoMapper.searchStockCurst(params));
        
        // 3. 공장 이벤트 그리드 : [{ MFG_TXT, LINE_TXT, EVENT_TXT, MFG_EVENT_MNG_SEQNO, USE_YN, VLD_STR_DT, VLD_END_DT }]
        params.put("TREEGRID_MFG_EVENT", m02010DaoMapper.searchMfgEvent(params));
        
        return params;
    }
    
    
    /**
     * 데이터 조회 : 재고현황 관리 그리드
     * @param params {  }
     * @return [{ ITEM_GROUP_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchStockCurstMng(Map<String, Object> params) throws Exception {
        return m02010DaoMapper.searchStockCurstMng(params);
    }

    /**
     * 데이터 저장 : 재고현황 관리 그리드
     * @param params { userId, saveData:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> saveStockCurstMng(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData"); // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> arrInsertList = new ArrayList<Map<String, Object>>(); // - [{ LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>(); // - [{ ITEM_GROUP_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
            List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>(); // - [{ ITEM_GROUP_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]

            int iInsertCnt = 0;
            int iUpdateCnt = 0;
            int iDeleteCnt = 0;

            // 1. 데이터 체크
            if(saveList == null || saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 분리 : 생성, 수정, 삭제로 분리
            for(Map<String, Object> mRow : saveList) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                mRow.put("userId", params.get("userId"));
                
                if(Const.ROW_STATUS_INSERT.equals(sAction)) {
                    arrInsertList.add(mRow);
                } else if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    arrUpdateList.add(mRow);
                } else if(Const.ROW_STATUS_DELETE.equals(sAction)) {
                    arrDeleteList.add(mRow);
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 정보가 없음).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // 3. 저장 처리
            if(arrDeleteList.size() > 0) {
                for(int i = 0 ; i < arrDeleteList.size() ; i++) {
                    iDeleteCnt += m02010DaoMapper.deleteStockCurstMng(arrDeleteList.get(i));
                }
            }
            if(arrUpdateList.size() > 0) {
                for(int i = 0 ; i < arrUpdateList.size() ; i++) {
                    iUpdateCnt += m02010DaoMapper.updateStockCurstMng(arrUpdateList.get(i));
                }
            }
            if(arrInsertList.size() > 0) {
                for(int i = 0 ; i < arrInsertList.size() ; i++) {
                    iInsertCnt += m02010DaoMapper.insertStockCurstMng(arrInsertList.get(i));
                }
            }
            
            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("INSERT_CNT", iInsertCnt);
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }

    
    /**
     * 데이터 조회 : 공장 이벤트 관리 그리드
     * @param params { bssYYYY }
     * @return [{ MFG_EVENT_MNG_SEQNO, MFG_TXT, LINE_TXT, EVENT_TXT, USE_YN, VLD_STR_DT, VLD_END_DT }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchMfgEventMng(Map<String, Object> params) throws Exception {
        return m02010DaoMapper.searchMfgEventMng(params);
    }

    /**
     * 데이터 저장 : 공장 이벤트 관리 그리드
     * @param params { userId, saveData:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> saveMfgEventMng(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData"); // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> arrInsertList = new ArrayList<Map<String, Object>>(); // - [{ MFG_TXT, LINE_TXT, EVENT_TXT, USE_YN, VLD_STR_DT, VLD_END_DT }]
            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>(); // - [{ MFG_EVENT_MNG_SEQNO, MFG_TXT, LINE_TXT, EVENT_TXT, USE_YN, VLD_STR_DT, VLD_END_DT }]
            List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>(); // - [{ MFG_EVENT_MNG_SEQNO, MFG_TXT, LINE_TXT, EVENT_TXT, USE_YN, VLD_STR_DT, VLD_END_DT }]

            int iInsertCnt = 0;
            int iUpdateCnt = 0;
            int iDeleteCnt = 0;

            // 1. 데이터 체크
            if(saveList == null || saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 분리 : 생성, 수정, 삭제로 분리
            for(Map<String, Object> mRow : saveList) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                mRow.put("userId", params.get("userId"));
                
                if(Const.ROW_STATUS_INSERT.equals(sAction)) {
                    arrInsertList.add(mRow);
                } else if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    arrUpdateList.add(mRow);
                } else if(Const.ROW_STATUS_DELETE.equals(sAction)) {
                    arrDeleteList.add(mRow);
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 정보가 없음).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // 3. 저장 처리
            if(arrDeleteList.size() > 0) {
                for(int i = 0 ; i < arrDeleteList.size() ; i++) {
                    iDeleteCnt += m02010DaoMapper.deleteMfgEventMng(arrDeleteList.get(i));
                }
            }
            if(arrUpdateList.size() > 0) {
                for(int i = 0 ; i < arrUpdateList.size() ; i++) {
                    iUpdateCnt += m02010DaoMapper.updateMfgEventMng(arrUpdateList.get(i));
                }
            }
            if(arrInsertList.size() > 0) {
                for(int i = 0 ; i < arrInsertList.size() ; i++) {
                    iInsertCnt += m02010DaoMapper.insertMfgEventMng(arrInsertList.get(i));
                }
            }
            
            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("INSERT_CNT", iInsertCnt);
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
}
