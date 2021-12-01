package com.hitejinro.snop.system.service;

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
import com.hitejinro.snop.system.dao.M08140DaoMapper;

/**
 * 사용자 관리
 * @author 김남현
 *
 */
@Service
public class M08140Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08140Service.class);

    @Inject
    private M08140DaoMapper m08140DaoMapper;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 사용자권한 조회
     * @param params { searchType, searchWord, useYN }
     * @return { TREEGRID_DATA:[{}], AUTH_LIST:[{}], ORG_LIST:[{}] }
     * @throws Exception
     */
    public Map<String, Object> search(Map<String, Object> params) throws Exception {
        if(params == null) params = new HashMap<String, Object>();

        // 1. 그리드 데이터 조회 : [{ EMPLOYEE_NUMBER, USER_NM, USER_ID, DEPT_CD, DEPT_NM, AUTH_CD, POSITION_CODE, POSITION_NAME, DUTY_CODE, DUTY_NAME, OFFICE_EMAIL, MOBILE_PHONE, USE_YN, AUTO_AUTH_LOCK_YN, AUTH_ORG_LEVEL, AUTH_ORG_CODE, AUTH_ADD_ORG_CODE_LIST, LAST_UPDATE_DATE, LAST_UPDATED_BY }]
        params.put("TREEGRID_DATA", m08140DaoMapper.search(params));

        // 2. 권한 리스트 조회 : [{ CODE, NAME }]
        params.put("AUTH_LIST", m08140DaoMapper.selectAuthList(params));

        // 3. 조직(권역/지점) 리스트 조회 : [{ DEPARTMENT_CODE, DEPARTMENT_KOREAN_NAME }]
        params.put("ORG_LIST", m08140DaoMapper.selectOrgList(params));
        
        return params;
    }
    
    /**
     * 사용자권한 저장
     * @param params { saveData:[{}], changeRowInfoList:[{}], userId }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> save(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData"); // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> changeRowInfoList = (List<Map<String, Object>>) params.get("changeRowInfoList"); // - ROW_INFO : [ {id, Changed, Deleted, Added, ChangeStatus, ChangeColIds[] }]

            List<Map<String, Object>> updateListByUser = new ArrayList<Map<String, Object>>(); // - USE_YN, AUTO_AUTH_LOCK_YN, AUTH_ORG_LEVEL : [{ USER_ID, USE_YN, AUTO_AUTH_LOCK_YN, AUTH_ORG_LEVEL, AUTH_CD, AUTH_ADD_ORG_CODE_LIST }]
            List<Map<String, Object>> updateListByAuth = new ArrayList<Map<String, Object>>(); // - AUTH_CD : [{ USER_ID, USE_YN, AUTO_AUTH_LOCK_YN, AUTH_ORG_LEVEL, AUTH_CD, AUTH_ADD_ORG_CODE_LIST }]
            List<Map<String, Object>> updateListByAddOrg = new ArrayList<Map<String, Object>>(); // - AUTH_ADD_ORG_CODE_LIST : [{ USER_ID, USE_YN, AUTO_AUTH_LOCK_YN, AUTH_ORG_LEVEL, AUTH_CD, AUTH_ADD_ORG_CODE_LIST }]

            int iUpdateCntByUser = 0;
            int iUpdateCntByAuth = 0;
            int iUpdateCntByAddOrg = 0;

            // 1. 데이터 체크
            if(saveList == null || saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 분리 : 변경된 행과 컬럼을 이용해서, 변경된 셀 정보 생성. 
            for(Map<String, Object> mRow : saveList) {
                // mRow : [{ USER_ID, USE_YN, AUTO_AUTH_LOCK_YN, AUTH_ORG_LEVEL, AUTH_CD, AUTH_ADD_ORG_CODE_LIST, ... }]
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                String sRowId = (mRow == null ? "" : (String)mRow.get("id"));
                
                mRow.put("userId", params.get("userId"));
                
                if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
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
                    boolean bHasUser = false; // - 사용자 정보(SCMU.W_SOP_TB_USER_M)를 변경하는지 여부. 체크 컬럼이 3개라서, 동일한 행을 추가하지 않기 위해서
                    for(int c = 0 ; c < arrChangeColIds.size() ; c++) {
                        String sColId = (String)arrChangeColIds.get(c);
                        if(("USE_YN".equals(sColId) || "AUTO_AUTH_LOCK_YN".equals(sColId) || "AUTH_ORG_LEVEL".equals(sColId)) && !bHasUser) {
                            updateListByUser.add(mRow);
                            bHasUser = true;
                        } else if("AUTH_CD".equals(sColId)) {
                            updateListByAuth.add(mRow);
                        } else if("AUTH_ADD_ORG_CODE_LIST".equals(sColId)) {
                            updateListByAddOrg.add(mRow);
                        } else {
                            mResult.put(Const.RESULT_FLAG, "F");
                            mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(허용되지 않은 컬럼 변경).");
                            mResult.put("ROW_DATA", mRow);
                            mResult.put("CHANGE_COL_IDS", arrChangeColIds);
                            commonUtils.debugParams(mResult);
                            return mResult;
                        }
                    }

                    // 3. 저장 처리
                    if(updateListByUser.size() < 1 && updateListByAuth.size() < 1 && updateListByAddOrg.size() < 1) {
                        mResult.put(Const.RESULT_FLAG, "F");
                        mResult.put(Const.RESULT_MSG, "저장할 데이터가 존재하지 않습니다. 정상적인 방법으로 처리하였는지 확인 바랍니다.");
                        commonUtils.debugParams(mResult);
                        return mResult;
                    }
                    // 3.1. 사용자 테이블 처리
                    if(updateListByUser.size() > 0) {
                        Map<String, Object> oSaveParam = new HashMap<String, Object>();
                        oSaveParam.putAll(params);
                        oSaveParam.put("updateList", updateListByUser);
                        iUpdateCntByUser = m08140DaoMapper.update(oSaveParam);
                    }
                    // 3.2. 사용자 권한 테이블 처리 : NA는 삭제, 그외는 변경/추가
                    if(updateListByAuth.size() > 0) {
                        Map<String, Object> oSaveParam = new HashMap<String, Object>();
                        oSaveParam.putAll(params);
                        oSaveParam.put("updateList", updateListByAuth);
                        iUpdateCntByAuth += m08140DaoMapper.deleteAuthUser(oSaveParam);
                        iUpdateCntByAuth += m08140DaoMapper.updateAuthUser(oSaveParam);
                    }
                    // 3.3. 사용자 추가 조직권한 테이블 처리 : 삭제 후 추가하는 방식으로 처리
                    if(updateListByAddOrg.size() > 0) {
                        for(int r = 0 ; r < updateListByAddOrg.size() ; r++) {
                            m08140DaoMapper.deleteUserAuthAddDept(updateListByAddOrg.get(r));
                            iUpdateCntByAddOrg += m08140DaoMapper.insertUserAuthAddDept(updateListByAddOrg.get(r));
                        }
                    }

                    // - 최종 처리
                    mResult.put(Const.RESULT_FLAG, "S");
                    mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
                    mResult.put("USER_UPDATE_CNT", iUpdateCntByUser);
                    mResult.put("AUTH_UPDATE_CNT", iUpdateCntByAuth);
                    mResult.put("ADD_ORG_UPDATE_CNT", iUpdateCntByAddOrg);
                    
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
    
}
