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
import com.hitejinro.snop.work.dao.M02030DaoMapper;

/**
 * 제품수급 > 연간 제품수급 Simul
 * @author 유기후
 *
 */
@Service
public class M02030Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M02030Service.class);

    @Inject
    private M02030DaoMapper m02030DaoMapper;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 데이터 조회
     * @param params { bssYYYY, useYn }
     * @return [{ VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN, WORK_INFO_10, WORK_INFO_20 }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m02030DaoMapper.search(params);
        return list;
    }

    /**
     * 데이터 저장
     * @param params { userId, saveData:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> save(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
            List<Map<String, Object>> arrInsertList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>();

            int iInsertCnt = 0;
            int iUpdateCnt = 0;

            // 1. 데이터 체크
            if (saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 분리 : 추가/수정만 존재함
            for (Map<String, Object> mRow : saveList) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                mRow.putAll(params); // - 사용자정보 등 넣기

                if(Const.ROW_STATUS_INSERT.equals(sAction)) {
                    arrInsertList.add(mRow);
                } else if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    arrUpdateList.add(mRow);
                } else if(Const.ROW_STATUS_DELETE.equals(sAction)) {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(삭제는 불가).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // 3. 데이터 저장 : 추가/수정 처리.
            // 3.1. 데이터 저장 - 수정
            for(int r = 0 ; r < arrUpdateList.size() ; r++) {
                iUpdateCnt += m02030DaoMapper.update(arrUpdateList.get(r));
            }
            if(arrUpdateList.size() != iUpdateCnt) {
                throw new Exception("추가/수정 중 오류 발생 : 수정 대상(" + arrUpdateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
            }
            // 3.2. 데이터 저장 - 추가
            for(int r = 0 ; r < arrInsertList.size() ; r++) {
                // - 신규 버전코드 채번 : 기준년월+일련번호 3자리. ex) 20210719-001
                List<Map<String, Object>> arrNewVerCdList = m02030DaoMapper.selectNewVerCd(arrInsertList.get(r));
                if(arrNewVerCdList != null && arrNewVerCdList.size() == 1 && !StringUtils.isEmpty(arrNewVerCdList.get(0).get("NEW_VER_CD"))) {
                    arrInsertList.get(r).put("NEW_VER_CD", arrNewVerCdList.get(0).get("NEW_VER_CD"));
                } else {
                    throw new Exception("추가/수정 중 오류 발생 : 추가 대상에 대한 신규 버전 채번 중 오류 발생");
                }
                iInsertCnt += m02030DaoMapper.insert(arrInsertList.get(r));
            }
            if(arrInsertList.size() != iInsertCnt) {
                throw new Exception("추가/수정 중 오류 발생 : 추가 대상(" + arrInsertList.size() + "건)과 처리된 대상(" + iInsertCnt + "건) 건수가 다릅니다");
            }

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("INSERT_CNT", iInsertCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }


    
    /**
     * 버전 정보 조회
     * @param params { verCd }
     * @return { VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }
     * @throws Exception
     */
    public Map<String, Object> selectVerInfo(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = m02030DaoMapper.selectVerInfo(params);
        Map<String, Object> mVerInfo = new HashMap<String, Object>();
        if(list != null && list.size() > 0) {
            mVerInfo = list.get(0);
        }
        return mVerInfo;
    }
    
    
    
    /**
     * 생산변수 리스트 조회
     * @param params {  }
     * @return [{ PRDT_VAR_VER_CD, PRDT_VAR_VER_NM }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectPrdtVarList(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m02030DaoMapper.selectPrdtVarList(params);
        return list;
    }

    /**
     * 생산변수 저장
     * @param params { userId, verCd, prdtVarVerCd }
     * @return 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    public Map<String, Object> savePrdtVarVerCd(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            int iUpdateCnt = 0;

            // 1. 데이터 체크
            if (StringUtils.isEmpty(params.get("verCd"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "버전 정보가 존재하지 않습니다. 창을 닫고 다시 시도해주세요.");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if (StringUtils.isEmpty(params.get("prdtVarVerCd"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 생산변수 정보가 없습니다. 창을 닫고 다시 시도해주세요.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 데이터 저장
            iUpdateCnt = m02030DaoMapper.updatePrdtVarVerCd(params);
            if(iUpdateCnt != 1) {
                throw new Exception("생산변수 저장 중 오류 발생 : 저장할 버전이 존재하지 않습니다.");
            }

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
    
    /**
     * 생산설정의 데이터 조회 : Header, Body 둘다 조회. 추가로 주간근무일수유형별 근무형태 리스트 조회
     * @param params { verCd, liquorCode }] }
     * @return { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }], WEEK_WORK_DCNT_TP_LIST:[{  }] }
     * @throws Exception
     */
    public Map<String, Object> searchPrdtSet(Map<String, Object> params) throws Exception {
        if(params == null) params = new HashMap<String, Object>();
        
        // 1. Header 조회 : 공장/라인의 컬럼 구성 리스트 : [{ VER_CD, STD_YYYYMMDD, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, PRDT_VAR_VER_USE_YN, LIQUOR_CODE, ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME, NEW_LINE_YN, COL_ID, HEADER_COL_SPAN }]
        params.put("TREEGRID_HEADER", m02030DaoMapper.searchPrdtSetHeader(params));
        
        // 2. Body 조회 : 헤더를 이용해서, 가변 컬럼 데이터 조회 : [{ VER_CD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, BUSINESS_DAY_FLAG, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, HAS_SAT_YN, COL_1_SFT_PTRN_DTY_CODE, COL_1_AVL_HR, COL_1_MAX_CONV_QTY_PER_HR, ... }]
        params.put("TREEGRID_BODY", m02030DaoMapper.searchPrdtSetBody(params));
        
        // 3. 주간근무일수유형별 근무형태 리스트(주당 생산기준 관리) 조회 : [{ LIQUOR_CODE, WEEK_WORK_DCNT_TP_CODE, WEEK_WORK_DCNT_TP_NAME, CALENDAR_WORK_CNT, WEEK_WORK_CNT, SFT_LIST, TOTAL_AVL_HR, SFT_REVERSE_LIST, REVERSE_TOTAL_AVL_HR }]
        params.put("WEEK_WORK_DCNT_TP_LIST", m02030DaoMapper.selectSftPtrnDtyByWeekWorkDcntTpList(params));
        
        return params;
    }

    /**
     * 생산설정의 데이터 저장
     * @param params { userId, saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}], VER_INFO:{}, COL_ACCT_LIST:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> savePrdtSet(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> arrSaveList = (List<Map<String, Object>>) params.get("saveData");                     // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> arrChangeRowInfoList = (List<Map<String, Object>>) params.get("changeRowInfoList");   // - ROW_INFO : [ {id, Changed, Deleted, Added, ChangeStatus, ChangeColIds[] }]
            List<Map<String, Object>> arrHeaderList = (List<Map<String, Object>>) params.get("TREEGRID_HEADER");            // - [{ VER_CD, STD_YYYYMMDD, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, PRDT_VAR_VER_USE_YN, LIQUOR_CODE, ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME, NEW_LINE_YN, COL_ID, HEADER_COL_SPAN }]
            List<Map<String, Object>> arrColAcctList = (List<Map<String, Object>>) params.get("COL_ACCT_LIST");             // - 근무형태(SFT_PTRN_DTY_CODE), 근무시간(AVL_HR) 계정 리스트 : [{ ACCT_CD, ACCT_NM }]
            Map<String, Object> mVerInfo = (Map<String, Object>) params.get("VER_INFO");                                    // - 버전 정보 : { VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }
            
            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>(); // - [{ _PK(VER_CD + ":" + LIQUOR_CODE + ":" + ORG_CODE + ":" + LINE_DEPT_CODE + ":" + PERIOD_YYYYMM + ":" + PERIOD_TYPE + ":" + PERIOD_CODE), VER_CD, LIQUOR_CODE, ORG_CODE, LINE_DEPT_CODE, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, SFT_PTRN_DTY_CODE, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD }]
            
            Map<String, Map<String, Object>> mHeaderListByColId = new HashMap<String, Map<String, Object>>(); // - 그리드의 헤더정보를 COL_ID를 키로 한 Map 형식으로 변경 : { COL_ID{ ORG_CODE, LINE_DEPT_CODE, COL_ID } }

            int iUpdateCnt = 0;

            // 1. 데이터 체크
            if(arrSaveList == null || arrSaveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(arrHeaderList == null || arrHeaderList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "변경 데이터에 대한 정보가 이상합니다.(헤더 오류)");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(arrColAcctList == null || arrColAcctList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "변경 데이터에 대한 정보가 이상합니다.(계정정보 오류)");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(mVerInfo == null || mVerInfo.isEmpty()) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "변경 데이터에 대한 정보가 이상합니다.(버전정보 오류)");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // - 체크를 편하게 하기 위한 데이터 변환
            for(Map<String, Object> mHeaderCol : arrHeaderList) {
                mHeaderListByColId.put((String)mHeaderCol.get("COL_ID"), mHeaderCol);
            }

            // 2. 저장 데이터 분리 : 변경된 행과 컬럼을 이용해서, 변경된 셀 정보 생성. 기간구분은 YYYYWW 만 대상. 근무형태(SFT_PTRN_DTY_CODE)만 받아서, 근무시간(AVL_HR)은 DB에서 재계산.
            for(Map<String, Object> mRow : arrSaveList) {
                // mRow : [{ VER_CD, LIQUOR_CODE, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, COL_1_SFT_PTRN_DTY_CODE, COL_1_AVL_HR, COL_1_MAX_CONV_QTY_PER_HR, ... }]
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                String sRowId = (mRow == null ? "" : (String)mRow.get("id"));
                String sVerCd = (mRow == null ? "" : (String)mRow.get("VER_CD"));
                String sLiquorCode = (mRow == null ? "" : (String)mRow.get("LIQUOR_CODE"));
                String sPeriodYYYYMM = (mRow == null ? "" : (String)mRow.get("PERIOD_YYYYMM"));
                String sPeriodType = (mRow == null ? "" : (String)mRow.get("PERIOD_TYPE"));
                String sPeriodCode = (mRow == null ? "" : (String)mRow.get("PERIOD_CODE"));
                String sPeriodFrYYYYMMDD = (mRow == null ? "" : (String)mRow.get("PERIOD_FR_YYYYMMDD"));
                String sPeriodToYYYYMMDD = (mRow == null ? "" : (String)mRow.get("PERIOD_TO_YYYYMMDD"));
                
                if(StringUtils.isEmpty(sVerCd) || StringUtils.isEmpty(sLiquorCode) || StringUtils.isEmpty(sPeriodYYYYMM) || StringUtils.isEmpty(sPeriodType) || StringUtils.isEmpty(sPeriodCode)) {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 행에 필수 정보 누락).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                    
                } else if(!sVerCd.equals(mVerInfo.get("VER_CD"))) {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(버전 정보가 그리드와 상이함).");
                    mResult.put("ROW_DATA", mRow);
                    mResult.put("VER_INFO", mVerInfo);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
                
                if(!"YYYYWW".equals(sPeriodType)) continue;
                
                if(Const.ROW_STATUS_UPDATE.equals(sAction) && !StringUtils.isEmpty(sRowId)) {
                    // 2.1. 변경된 행에서 변경된 컬럼 정보만 뽑아서, 저장용 데이터 생성 : 단, 근무형태(SFT_PTRN_DTY_CODE) 컬럼만 대상
                    List<String> arrChangeColIds = new ArrayList<String>();
                    for(int j = 0 ; j < arrChangeRowInfoList.size() ; j++) {
                        if(sRowId.equals((String)arrChangeRowInfoList.get(j).get("id"))) {
                            arrChangeColIds = (List<String>)arrChangeRowInfoList.get(j).get("ChangeColIds");
                            j = arrChangeRowInfoList.size();
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
                        if(StringUtils.isEmpty(sColId) || sColId.indexOf("_SFT_PTRN_DTY_CODE") < 0) continue;
                        String sOrgLineColId = sColId.substring(0, sColId.indexOf("_SFT_PTRN_DTY_CODE")); // - 헤더정보(공장+라인)에서 정의된 COL_ID 값
                        Map<String, Object> mColInfo = mHeaderListByColId.get(sOrgLineColId); // - 변경된 COL_ID를 이용해서, 헤더에서 정보를 수집 : {ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME, NEW_LINE_YN, COL_ID }
                        String sOrgCode = (mColInfo.get("ORG_CODE") == null ? "" : (String)mColInfo.get("ORG_CODE"));
                        String sLineDeptCode = (mColInfo.get("LINE_DEPT_CODE") == null ? "" : (String)mColInfo.get("LINE_DEPT_CODE"));

                        String sRowPK = sVerCd + ":" + sLiquorCode + ":" + sOrgCode + ":" + sLineDeptCode + ":" + sPeriodYYYYMM + ":" + sPeriodType + ":" + sPeriodCode;
                        if(StringUtils.isEmpty(sOrgCode) || StringUtils.isEmpty(sLineDeptCode)) {
                            mResult.put(Const.RESULT_FLAG, "F");
                            mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 열에 공장/라인 정보 누락).");
                            mResult.put("ROW_DATA", mRow);
                            mResult.put("COL_ID", sColId);
                            mResult.put("COL_INFO", mColInfo);
                            commonUtils.debugParams(mResult);
                            return mResult;
                        }
                        // - 이미 생성되어있는지 체크 : 생성되어있으면 오류.
                        boolean bFlag = false;
                        for(int k = 0 ; k < arrUpdateList.size() ; k++) {
                            if(sRowPK.equals((String)arrUpdateList.get(k).get("_PK"))) {
                                bFlag = true;
                                k = arrUpdateList.size();
                            }
                        }
                        if(bFlag) {
                            mResult.put(Const.RESULT_FLAG, "F");
                            mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(동일한 공장/라인 데이터가 존재함).");
                            mResult.put("ROW_DATA", mRow);
                            mResult.put("COL_ID", sColId);
                            mResult.put("COL_INFO", mColInfo);
                            commonUtils.debugParams(mResult);
                            return mResult;
                        }
                        
                        Map<String, Object> mUpdateRow = new HashMap<String, Object>(); // - 저장용 행 데이터 : { _PK, VER_CD, LIQUOR_CODE, ORG_CODE, LINE_DEPT_CODE, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, SFT_PTRN_DTY_CODE, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD }

                        mUpdateRow.put("_PK", sRowPK);
                        mUpdateRow.put("VER_CD", sVerCd);
                        mUpdateRow.put("LIQUOR_CODE", sLiquorCode);
                        mUpdateRow.put("ORG_CODE", sOrgCode);
                        mUpdateRow.put("LINE_DEPT_CODE", sLineDeptCode);
                        mUpdateRow.put("PERIOD_YYYYMM", sPeriodYYYYMM);
                        mUpdateRow.put("PERIOD_TYPE", sPeriodType);
                        mUpdateRow.put("PERIOD_CODE", sPeriodCode);
                        mUpdateRow.put("SFT_PTRN_DTY_CODE", mRow.get(sColId));
                        mUpdateRow.put("PERIOD_FR_YYYYMMDD", sPeriodFrYYYYMMDD);
                        mUpdateRow.put("PERIOD_TO_YYYYMMDD", sPeriodToYYYYMMDD);
                        mUpdateRow.put("WEEK_WORK_CNT", mRow.get("WORK_CNT"));
                        mUpdateRow.put("CALENDAR_WORK_CNT", mRow.get("CALENDAR_WORK_CNT"));
                        arrUpdateList.add(mUpdateRow);
                    }
                    
                }
            }
            
            // 3. 저장 처리
            if(arrUpdateList == null || arrUpdateList.size() < 1) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 존재하지 않습니다(근무형태 변경사항만 저장 대상). 정상적인 방법으로 처리하였는지 확인 바랍니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            // - 별도의 Map에 담기 : 화면단에서 넘어온 것과 분리
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.putAll(params);
            oSaveParam.put("updateList", arrUpdateList);
            iUpdateCnt += m02030DaoMapper.deletePrdtSet(oSaveParam); // - 근무형태가 N/A인 것은 삭제 처리
            iUpdateCnt += m02030DaoMapper.updatePrdtSet(oSaveParam);
            
            // 4. 작업정보 저장
            m02030DaoMapper.updateVerWorkInfo(oSaveParam);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }

    
    /**
     * 생산설정 - 생산설정 갱신
     * @param params { VER_INFO:{} }
     * @return 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    public Map<String, Object> refreshPrdtSet(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            Map<String, Object> mVerInfo = (Map<String, Object>) params.get("VER_INFO");                                    // - 버전 정보 : { VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }

            int iUpdateCnt = 0;
            int iDeleteCnt = 0;

            // 1. 데이터 체크
            if(mVerInfo == null || mVerInfo.isEmpty() || StringUtils.isEmpty(mVerInfo.get("VER_CD"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "버전정보가 존재하지 않습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 처리
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.putAll(params);
            oSaveParam.put("verCd", mVerInfo.get("VER_CD"));
            // 2.1. 생산설정 갱신 초기화 : 허용되지 않는 근무형태와 공장/라인 리스트 삭제
            iDeleteCnt += m02030DaoMapper.deletePrdtSetRefresh(oSaveParam);
            // 2.2. 생산설정 갱신 Update : 근무형태에 따른 가용시간과 공장/라인의 명칭 Update
            iUpdateCnt += m02030DaoMapper.updatePrdtSetRefresh(oSaveParam);

            // 3. 작업정보 저장
            oSaveParam.put("liquorCode", "!ALL"); // - 생산설정 갱신은 맥주/소주 모두 처리
            m02030DaoMapper.updateVerWorkInfo(oSaveParam);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "생산변수 재반영이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
    
    
    
    /**
     * 판매설정의 데이터 조회
     * @param params { verCd, liquorCode, vesselCode }] }
     * @return [{ VER_CD, STD_YYYYMMDD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, Def, PERIOD_DESC, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, USE_YYYY_TRG_SALE_CONV_QTY, USE_ESPN_ACTUAL_SALE_CONV_QTY, RMKS, ACTUAL_SALE_CONV_QTY, YYYY_TRG_SALE_CONV_QTY, ESPN_SALE_CONV_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleSet(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m02030DaoMapper.searchSaleSet(params);
        return list;
    }


    /**
     * 버전의 기간 리스트(년월+연주차) : 판매변수의 적용기간 설정시 사용
     * @param params { verCd }
     * @return [{ CODE, NAME, VER_CD, YYYYMM, SCM_YYYYWW }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectPeriodList(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m02030DaoMapper.selectPeriodList(params);
        return list;
    }
    
    /**
     * 판매설정 - 판매변수 조회
     * @param params { verCd }
     * @return [{ VER_CD, SALE_SET_TYPE_CODE, SALE_SET_TYPE_NAME, SEQNO, SALE_VAR_APL_FR_PERIOD, SALE_VAR_APL_TO_PERIOD, SALE_VAR_APL_FR_YYYYMM, SALE_VAR_APL_FR_SCM_YYYYWW, SALE_VAR_APL_TO_YYYYMM, SALE_VAR_APL_TO_SCM_YYYYWW, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, SALE_VAR_VAL, LIQUOR_CODE, LIQUOR_DESC, SALE_VAR_USAGE_NAME, SALE_VAR_ITEM_NAME, SALE_VAR_NAME, SALE_VAR_TYPE, SALE_VAR_USAGE_CODE, SALE_VAR_ITEM_CODE, SALE_VAR_APL_DIF_TYPE, SALE_VAR_VALID_MSG }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleVar(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m02030DaoMapper.searchSaleVar(params);
        return list;
    }

    /**
     * 판매설정 - 판매변수 저장
     * @param params { saveData:[{}], VER_INFO:{} }
     * @return 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    public Map<String, Object> saveSaleVar(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            // - [{ VER_CD, SALE_SET_TYPE_CODE, SALE_SET_TYPE_NAME, SEQNO, SALE_VAR_APL_FR_PERIOD, SALE_VAR_APL_TO_PERIOD, SALE_VAR_APL_FR_YYYYMM, SALE_VAR_APL_FR_SCM_YYYYWW, SALE_VAR_APL_TO_YYYYMM, SALE_VAR_APL_TO_SCM_YYYYWW, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, SALE_VAR_VAL, LIQUOR_CODE, LIQUOR_DESC, SALE_VAR_USAGE_NAME, SALE_VAR_ITEM_NAME, SALE_VAR_NAME, SALE_VAR_TYPE, SALE_VAR_USAGE_CODE, SALE_VAR_ITEM_CODE, SALE_VAR_APL_DIF_TYPE, SALE_VAR_VALID_MSG }]
            List<Map<String, Object>> arrSaveList = (List<Map<String, Object>>) params.get("saveData");                     // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            Map<String, Object> mVerInfo = (Map<String, Object>) params.get("VER_INFO");                                    // - 버전 정보 : { VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYYMM, USE_YN, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }

            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>(); // - 추가/수정 대상 리스트
            List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>(); // - 삭제 대상 리스트

            int iUpdateCnt = 0;
            int iDeleteCnt = 0;
            int iUpdateSyncCnt = 0; // - 판매변수의 상세 속성이 변경되어있으면 반영(화면단에서 변경한 변수는 자동 처리됨)
            int iDeleteSyncCnt = 0; // - 존재하지 않는 변수 삭제

            // 1. 데이터 체크
            if(arrSaveList == null || arrSaveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(mVerInfo == null || mVerInfo.isEmpty()) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "변경 데이터에 대한 정보가 이상합니다.(버전정보 오류)");
                commonUtils.debugParams(mResult);
                return mResult;
            }

            // 2. 저장 데이터 분리 : 추가/수정과 삭제로 구분
            for(Map<String, Object> mRow : arrSaveList) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                String sVerCd = (mRow == null ? "" : (String)mRow.get("VER_CD"));
                
                if(StringUtils.isEmpty(sVerCd)) {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 행에 필수 정보 누락).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                    
                } else if(!sVerCd.equals(mVerInfo.get("VER_CD"))) {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(버전 정보가 그리드와 상이함).");
                    mResult.put("ROW_DATA", mRow);
                    mResult.put("VER_INFO", mVerInfo);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
                
                if(Const.ROW_STATUS_INSERT.equals(sAction) || Const.ROW_STATUS_UPDATE.equals(sAction)) {
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
            
            // 3. 저장 처리 : 동기화는 변경사항에 관계없이 강제 호출해서 처리
            // 3.1. 삭제 처리
            if(arrDeleteList != null && arrDeleteList.size() > 0) {
                for(int r = 0 ; r < arrDeleteList.size() ; r++) {
                    Map<String, Object> oSaveParam = new HashMap<String, Object>();
                    oSaveParam.putAll(params);
                    oSaveParam.putAll(arrDeleteList.get(r));
                    iDeleteCnt += m02030DaoMapper.deleteSaleVar(oSaveParam);
                }
                if(iDeleteCnt != arrDeleteList.size()) {
                    throw new Exception("삭제 중 오류 발생 : 삭제할 대상(" + arrDeleteList.size() + "건)과 처리된 대상(" + iDeleteCnt + "건) 건수가 다릅니다");
                }
            }
            // 3.2. 추가/수정 처리
            if(arrUpdateList != null && arrUpdateList.size() > 0) {
                // - SEQNO를 추가시에 순차적으로 생성하기 위해서, 하나씩 쿼리문 실행
                for(int r = 0 ; r < arrUpdateList.size() ; r++) {
                    Map<String, Object> oSaveParam = new HashMap<String, Object>();
                    oSaveParam.putAll(params);
                    oSaveParam.putAll(arrUpdateList.get(r));
                    iUpdateCnt += m02030DaoMapper.updateSaleVar(oSaveParam);
                }
                if(iUpdateCnt != arrUpdateList.size()) {
                    throw new Exception("추가/수정 중 오류 발생 : 추가/수정할 대상(" + arrUpdateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
                }
            }
            
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.put("verCd", mVerInfo.get("VER_CD"));
            oSaveParam.putAll(params);
            
            // 3.3. 동기화 수정 : 판매변수의 상세 속성이 변경되어있으면 반영(화면단에서 변경한 변수는 자동 처리됨)
            iUpdateSyncCnt += m02030DaoMapper.updateSaleVarSync(oSaveParam);
            
            // 3.4. 동기화 삭제 : 존재하지 않는 변수 삭제
            iDeleteSyncCnt += m02030DaoMapper.deleteSaleVarSync(oSaveParam);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);
            mResult.put("UPDATE_SYNC_CNT", iUpdateSyncCnt);
            mResult.put("DELETE_SYNC_CNT", iDeleteSyncCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }


    /**
     * 판매설정 - 판매량 계산
     * @param params { VER_INFO:{}, liquorCode }
     * @return 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    public Map<String, Object> calculateSale(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            Map<String, Object> mVerInfo = (Map<String, Object>) params.get("VER_INFO");                                    // - 버전 정보 : { VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYYMM, USE_YN, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }

            int iUpdateCnt = 0;
            int iDeleteCnt = 0;

            // 1. 데이터 체크
            if(mVerInfo == null || mVerInfo.isEmpty() || StringUtils.isEmpty(mVerInfo.get("VER_CD"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "버전정보가 존재하지 않습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. Validation : N/A
            
            // 3. 저장 처리
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.putAll(params);
            oSaveParam.put("verCd", mVerInfo.get("VER_CD"));
            // 3.1. 초기화
            iDeleteCnt += m02030DaoMapper.deleteSale(oSaveParam);
            // 3.2. 생성
            iUpdateCnt += m02030DaoMapper.updateSale(oSaveParam);
            if(iUpdateCnt < 1) {
                throw new Exception("판매량 계산 중 오류 발생 : 생성된 항목이 존재하지 않음(창을 닫고 설정을 확인하고 다시 시도해보시고, 계속 발생하면 관리자에게 문의하세요)");
            }
            
            // 4. 작업정보 저장
            m02030DaoMapper.updateVerWorkInfo(oSaveParam);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "판매량 계산이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
    
    

    /**
     * 결과설정의 그리드 조회
     * @param params { verCd, liquorCode, vesselCode }
     * @return [{ VER_CD, STD_YYYYMMDD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, Def, PERIOD_DESC, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, YYYY_TRG_SALE_CONV_QTY, ESPN_ACTUAL_SALE_CONV_QTY, PRDT_PLAN_CONV_QTY, PRDT_ACTUAL_CONV_QTY, STOCK_CONV_QTY, BF_YYYY_STOCK_CONV_QTY, YYYY_TRG_SALE_CONV_AVG_QTY, STOCK_DCNT, AF_STRG_STOCK_DNCT, STRG_STOCK_CONV_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchResult(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m02030DaoMapper.searchResult(params);
        return list;
    }

    /**
     * 결과설정 - 계산
     * @param params { liquorCode, VER_INFO:{} }
     * @return 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    public Map<String, Object> calculateResult(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            Map<String, Object> mVerInfo = (Map<String, Object>) params.get("VER_INFO");                                    // - 버전 정보 : { VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYYMM, USE_YN, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }

            int iUpdateCnt = 0;
            int iDeleteCnt = 0;

            // 1. 데이터 체크
            if(mVerInfo == null || mVerInfo.isEmpty() || StringUtils.isEmpty(mVerInfo.get("VER_CD"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "버전정보가 존재하지 않습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. Validation : N/A
            
            // 3. 저장 처리
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.putAll(params);
            oSaveParam.put("verCd", mVerInfo.get("VER_CD"));
            // 3.1. 초기화
            iDeleteCnt += m02030DaoMapper.deleteResult(oSaveParam);
            // 3.2. 생성
            iUpdateCnt += m02030DaoMapper.updateResult(oSaveParam);
            if(iUpdateCnt < 1) {
                throw new Exception("시뮬레이션 계산 중 오류 발생 : 생성된 항목이 존재하지 않음(창을 닫고 설정을 확인하고 다시 시도해보시고, 계속 발생하면 관리자에게 문의하세요)");
            }
            
            // 4. 작업정보 저장
            m02030DaoMapper.updateVerWorkInfo(oSaveParam);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "시뮬레이션 계산이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
    

}
