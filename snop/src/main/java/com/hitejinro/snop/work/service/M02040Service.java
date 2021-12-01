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
import org.springframework.util.StringUtils;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.work.dao.M02040DaoMapper;

/**
 * 제품수급 > 계획대비 실적 차이 분석
 * @author 유기후
 *
 */
@Service
public class M02040Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M02040Service.class);

    @Inject
    private M02040DaoMapper m02040DaoMapper;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 데이터 조회 : Header, Body 둘다 조회
     * @param params { fromYYYYMM, toYYYYMM, liquorCode, usageCode, vesselCode, volumeValue, acctCd(ACCT_UOM_CS_QTY(환산c/s), ACCT_CS_QTY(단순c/s)) }] }
     * @return { TREEGRID_HEADER[{  }], TREEGRID_BODY[{  }] }
     * @throws Exception
     */
    public Map<String, Object> search(Map<String, Object> params) throws Exception {
        if(params == null) params = new HashMap<String, Object>();
        
        // 1. Header 조회 : 조회기간에 따른 컬럼 구성 리스트 : [{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC, MAX_ACTUAL_SALE_QTY_DT, MAX_ACTUAL_SALE_QTY_YYYYWW_YN }]
        params.put("TREEGRID_HEADER", m02040DaoMapper.searchHeader(params));
        
        // 2. Body 조회 : 헤더를 이용해서, 가변 컬럼 데이터 조회 :
        params.put("TREEGRID_BODY", m02040DaoMapper.searchBody(params));
        
        return params;
    }

    /**
     * 데이터 저장
     * @param params { fromYYYYMM, toYYYYMM, liquorCode, usageCode, vesselCode, volumeValue, acctCd, saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> save(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData"); // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> changeRowInfoList = (List<Map<String, Object>>) params.get("changeRowInfoList"); // - ROW_INFO : [ {id, Changed, Deleted, Added, ChangeStatus, ChangeColIds[] }]
            List<Map<String, Object>> headerList = (List<Map<String, Object>>) params.get("TREEGRID_HEADER"); // - [{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC, MAX_ACTUAL_SALE_QTY_DT, MAX_ACTUAL_SALE_QTY_YYYYWW_YN }]
            List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>(); // - [{ _PK(PERIOD_YYYYMM + ":" + PERIOD_MMWW + ":" + LIQUOR_CODE), PERIOD_YYYYMM, PERIOD_MMWW, LIQUOR_CODE, SALE_RSN, PRDT_RSN, CHANGE_SALE_YN, CHANGE_PRDT_YN }]
            
            Map<String, Map<String, Object>> mHeaderListByColId = new HashMap<String, Map<String, Object>>(); // - 그리드의 헤더정보를 COL_ID를 키로 한 Map 형식으로 변경 : { COL_ID{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC } }

            int iUpdateCnt = 0;

            // 1. 데이터 체크
            if(saveList == null || saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(headerList == null || headerList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "변경 데이터에 대한 정보가 이상합니다.(헤더 오류)");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // - 체크를 편하게 하기 위한 데이터 변환
            for(Map<String, Object> mHeaderCol : headerList) {
                mHeaderListByColId.put((String)mHeaderCol.get("COL_ID"), mHeaderCol);
            }
            
            // 2. 저장 데이터 분리 : 변경된 행과 컬럼을 이용해서, 변경된 셀 정보 생성. 단, 판매사유(SALE_RSN), 생산사유(PRDT_RSN)는 그리드의 행은 다르지만, 하나의 Recode로 처리(Table 구조상)
            for(Map<String, Object> mRow : saveList) {
                // mRow : [{ LIQUOR_CODE, GUBUN2_CODE, GUBUN3_CODE(SALE_RSN, PRDT_RSN), YMW20210100, ... }]
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                String sRowId = (mRow == null ? "" : (String)mRow.get("id"));
                String sLiquorCode = (mRow == null ? "" : (String)mRow.get("LIQUOR_CODE"));
                String sGubun3Code = (mRow == null ? "" : (String)mRow.get("GUBUN3_CODE")); // - "SALE_RSN"(판매사유), "PRDT_RSN"(생산사유)
                if(Const.ROW_STATUS_UPDATE.equals(sAction) && !StringUtils.isEmpty(sRowId) && ("SALE_RSN".equals(sGubun3Code) || "PRDT_RSN".equals(sGubun3Code))) {
                    // 2.1. 변경된 행에서 변경된 컬럼 정보만 뽑아서, 저장용 데이터 생성
                    List<String> arrChangeColIds = new ArrayList<String>();
                    for(int j = 0 ; j < changeRowInfoList.size() ; j++) {
                        if(sRowId.equals((String)changeRowInfoList.get(j).get("id"))) {
                            arrChangeColIds = (List<String>)changeRowInfoList.get(j).get("ChangeColIds");
                            j = changeRowInfoList.size();
                        }
                    }
                    if(arrChangeColIds == null || arrChangeColIds.size() < 1) {
                        mResult.put(Const.RESULT_FLAG, "F");
                        mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 행의 변경 정보가 없음).");
                        mResult.put("ROW_DATA", mRow);
                        commonUtils.debugParams(mResult);
                        return mResult;
                    }
                    for(int c = 0 ; c < arrChangeColIds.size() ; c++) {
                        String sColId = (String)arrChangeColIds.get(c);
                        Map<String, Object> mColInfo = mHeaderListByColId.get(sColId); // - 변경된 COL_ID를 이용해서, 헤더에서 정보를 수집 : { PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC }
                        String sPeriodYyyymm = (mColInfo.get("PERIOD_YYYYMM") == null ? "" : (String)mColInfo.get("PERIOD_YYYYMM"));
                        String sPeriodMmww = (mColInfo.get("PERIOD_MMWW") == null ? "" : (String)mColInfo.get("PERIOD_MMWW"));
                        Map<String, Object> mUpdateRow = new HashMap<String, Object>(); // - 저장용 행 데이터 : { _PK, PERIOD_YYYYMM, PERIOD_MMWW, LIQUOR_CODE, SALE_RSN, PRDT_RSN, CHANGE_SALE_YN, CHANGE_PRDT_YN }

                        String sRowPK = sPeriodYyyymm + ":" + sPeriodMmww + ":" + sLiquorCode;
                        // - 이미 생성되어있는지 체크 : 판매사유, 생산사유가 행이 달라서 체크 필요
                        for(int k = 0 ; k < updateList.size() ; k++) {
                            if(sRowPK.equals((String)updateList.get(k).get("_PK"))) {
                                mUpdateRow = updateList.get(k);
                                k = updateList.size();
                            }
                        }
                        if(mUpdateRow == null || mUpdateRow.isEmpty()) {
                            mUpdateRow = new HashMap<String, Object>();
                            mUpdateRow.put("_PK", sRowPK);
                            mUpdateRow.put("PERIOD_YYYYMM", sPeriodYyyymm);
                            mUpdateRow.put("PERIOD_MMWW", sPeriodMmww);
                            mUpdateRow.put("LIQUOR_CODE", sLiquorCode);
                            updateList.add(mUpdateRow);
                        }
                        if("SALE_RSN".equals(sGubun3Code)) {
                            mUpdateRow.put("CHANGE_SALE_YN", "Y");
                            mUpdateRow.put("SALE_RSN", mRow.get(sColId));
                        } else if("PRDT_RSN".equals(sGubun3Code)) {
                            mUpdateRow.put("CHANGE_PRDT_YN", "Y");
                            mUpdateRow.put("PRDT_RSN", mRow.get(sColId));
                        }
                    }
                    
                }
            }
            
            // 3. 저장 처리
            if(updateList == null || updateList.size() < 1) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 존재하지 않습니다. 정상적인 방법으로 처리하였는지 확인 바랍니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            // - 별도의 Map에 담기 : 화면단에서 넘어온 것과 분리
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.putAll(params);
            oSaveParam.put("updateList", updateList);
            iUpdateCnt = m02040DaoMapper.update(oSaveParam);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
}
