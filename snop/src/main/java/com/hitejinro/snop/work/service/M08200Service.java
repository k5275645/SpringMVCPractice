package com.hitejinro.snop.work.service;

import java.math.BigDecimal;
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
import com.hitejinro.snop.work.dao.M08200DaoMapper;

/**
 * 기준정보 > Alert 기능 종합
 * @author 유기후
 *
 */
@Service
public class M08200Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08200Service.class);

    @Inject
    private M08200DaoMapper m08200DaoMapper;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 데이터 조회 : Alert 리스트
     * @param params {  }
     * @return [{ ALERT_CODE, ALERT_NAME, ALERT_DESC, ALERT_TYPE, SEND_PERIOD_CODE, SEND_HR, SEND_TYPE, USE_YN, SEND_DEPT_TYPE, PROGRAM_NAME, MSG, RMKS, TARGET_CNT, CODE, NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m08200DaoMapper.search(params);
        return list;
    }
    
    /**
     * 데이터 저장 : Alert 리스트
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

            int iUpdateCnt = 0;

            // 1. 데이터 체크
            if(saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 체크 : 실제로는 업데이트만 존재
            for (Map<String, Object> mRow : saveList) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));

                if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    mRow.put("userId", params.get("userId"));
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // 3. 저장 처리 : 수정
            for(int i = 0 ; i < saveList.size() ; i++) {
                iUpdateCnt += m08200DaoMapper.update(saveList.get(i));
            }
            if(saveList.size() != iUpdateCnt) {
                throw new Exception("수정 중 오류 발생 : 수정 대상(" + saveList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
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
     * Alert 발송 호출 : 자동발송이 아닌 수동으로, 해당 Alert을 강제 발송 : SCMU.PKG_SOP_ALERT 참고
     * @param params { userId, alertCode, programName, O_TOT_ROWCOUNT, O_RETURN_MSG, O_RETURN_STATUS }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    public Map<String, Object> callAlertSend(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            // 1. 데이터 체크
            if(StringUtils.isEmpty(params.get("alertCode"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "호출 데이터에 대한 정보가 이상합니다.(Alert코드 없음)");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(StringUtils.isEmpty(params.get("programName"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "호출 데이터에 대한 정보가 이상합니다.(SP명 없음)");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            String sAlertCode = (String)params.get("alertCode");
            String sProgramName = (String)params.get("programName");
            
            // 2. 호출
            // - 별도의 Map에 담기 : 화면단에서 넘어온 것과 분리
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.put("alertCode", sAlertCode);
            oSaveParam.put("userId", params.get("userId"));
            
            if("SCMU.PKG_SOP_ALERT.SP_SEND_ACCUM_STOCK_OU".equals(sProgramName)) {
                m08200DaoMapper.callAlertSendAccumStockOu(oSaveParam); // - { O_TOT_ROWCOUNT, O_RETURN_MSG, O_RETURN_STATUS }
            } else if("SCMU.PKG_SOP_ALERT.SP_SEND_ACCUM_STOCK_DEPARTMENT".equals(sProgramName)) {
                m08200DaoMapper.callAlertSendAccumStockDepartment(oSaveParam); // - { O_TOT_ROWCOUNT, O_RETURN_MSG, O_RETURN_STATUS }
            } else {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "호출 데이터에 대한 정보가 이상합니다.(허용되지 않는 SP명)");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            commonUtils.debugParams(oSaveParam);

            // - 최종 처리
            if("S".equals(oSaveParam.get("O_RETURN_STATUS"))) {
                mResult.put(Const.RESULT_FLAG, "S");
                mResult.put(Const.RESULT_MSG, "발송이 성공하였습니다.");
                mResult.put("UPDATE_CNT", ((BigDecimal)oSaveParam.get("O_TOT_ROWCOUNT")).intValue());
            } else {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "발송이 실패하였습니다 : " + oSaveParam.get("O_RETURN_MSG"));
                commonUtils.debugParams(oSaveParam);
            }

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }


    
    /**
     * 사용자 리스트 조회
     * @param params {  }
     * @return [{ CODE, NAME, EMPLOYEE_NUMBER, EMPLOYEE_KOR_NAME, DUTY_CODE, DUTY_NAME, POSITION_CODE, POSITION_NAME, DEPARTMENT_CODE, DEPARTMENT_NAME, DEPT_CODE, DEPARTMENT_NAME_PATH }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectUserList(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m08200DaoMapper.selectUserList(params);
        return list;
    }

    /**
     * 사용자의 부서 리스트 조회
     * @param params {  }
     * @return [{ DEPARTMENT_CODE, DEPARTMENT_NAME, DEPT_CODE }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectUserDepartmentList(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m08200DaoMapper.selectUserDepartmentList(params);
        return list;
    }

    /**
     * 지점 리스트 조회
     * @param params {  }
     * @return [{ DEPT_CODE, DEPT_NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectDeptList(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m08200DaoMapper.selectDeptList(params);
        return list;
    }
    
    

    /**
     * Alert 대상자 데이터 조회
     * @param params { alertCode }
     * @return [{ ALERT_CODE, TARGET_TYPE, TARGET_VAL, SEND_DEPT_TYPE, SEND_DEPT_VAL, USE_YN }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchAlertTarget(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m08200DaoMapper.searchAlertTarget(params);
        return list;
    }
    
    /**
     * Alert 대상자 데이터 저장
     * @param params { userId, saveData:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> saveAlertTarget(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
            List<Map<String, Object>> arrInsertList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>();

            int iInsertCnt = 0;
            int iUpdateCnt = 0;
            int iDeleteCnt = 0;

            // 1. 데이터 체크
            if(saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 분리 : 
            for (Map<String, Object> mRow : saveList) {
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
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // 3. 저장 처리
            // 3.1. 삭제 처리
            for(int i = 0 ; i < arrDeleteList.size() ; i++) {
                iDeleteCnt += m08200DaoMapper.deleteAlertTarget(arrDeleteList.get(i));
            }
            if(arrDeleteList.size() != iDeleteCnt) {
                throw new Exception("저장 중 오류 발생 : 삭제 대상(" + arrDeleteList.size() + "건)과 처리된 대상(" + iDeleteCnt + "건) 건수가 다릅니다");
            }
            // 3.2. 수정 처리
            for(int i = 0 ; i < arrUpdateList.size() ; i++) {
                iUpdateCnt += m08200DaoMapper.updateAlertTarget(arrUpdateList.get(i));
            }
            if(arrUpdateList.size() != iUpdateCnt) {
                throw new Exception("저장 중 오류 발생 : 수정 대상(" + arrUpdateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
            }
            // 3.3. 추가 처리
            for(int i = 0 ; i < arrInsertList.size() ; i++) {
                iInsertCnt += m08200DaoMapper.insertAlertTarget(arrInsertList.get(i));
            }
            if(arrInsertList.size() != iInsertCnt) {
                throw new Exception("저장 중 오류 발생 : 추가 대상(" + arrInsertList.size() + "건)과 처리된 대상(" + iInsertCnt + "건) 건수가 다릅니다");
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
     * 수동 Alert 발송
     * @param params { msg, userId, saveData:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> sendManualAlert(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");

            int iInsertAlertCnt = 0;
            int iInsertAlertTargetCnt = 0;
            int iSendCnt = 0;

            // 1. 데이터 체크
            if(saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "발송할 대상자가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(StringUtils.isEmpty(params.get("msg"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "발송할 메시지가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 신규 수동 Alert 코드 채번
            List<Map<String, Object>> arrNewManualAlertList = m08200DaoMapper.selectNewManualAlertCode(params); // - [{ NEW_ALERT_CODE, NEW_DTM }]
            if(arrNewManualAlertList == null || arrNewManualAlertList.size() != 1 || StringUtils.isEmpty(arrNewManualAlertList.get(0).get("NEW_ALERT_CODE"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "신규 수동 Alert 코드를 채번하는 중에 알 수 없는 오류가 발생하였습니다. 관리자에게 문의하세요.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 3. 신규 수동 Alert 마스터 생성
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.putAll(params);
            oSaveParam.putAll(arrNewManualAlertList.get(0));
            iInsertAlertCnt = m08200DaoMapper.insertManualAlert(oSaveParam);
            if(iInsertAlertCnt != 1) {
                commonUtils.debugParams(oSaveParam);
                throw new Exception("신규 수동 Alert을 생성하는 중 알 수 없는 오류가 발생하였습니다. 관리자에게 문의하세요.");
            }
            
            // 4. 신규 수동 Alert 대상자 생성
            for(int i = 0 ; i < saveList.size() ; i++) {
                saveList.get(i).put("ALERT_CODE", oSaveParam.get("NEW_ALERT_CODE"));
                saveList.get(i).put("userId", oSaveParam.get("userId"));
                iInsertAlertTargetCnt += m08200DaoMapper.insertAlertTarget(saveList.get(i));
            }
            if(saveList.size() != iInsertAlertTargetCnt) {
                throw new Exception("신규 수동 Alert의 대상자 생성 중 오류 발생 : 추가 대상(" + saveList.size() + "건)과 처리된 대상(" + iInsertAlertTargetCnt + "건) 건수가 다릅니다");
            }
            
            // 5. 수동 Alert 발송 : XXSMS.SUREDATA
            iSendCnt += m08200DaoMapper.sendManualAlert(oSaveParam);
            if(iSendCnt < 1) {
                commonUtils.debugParams(oSaveParam);
                throw new Exception("신규 수동 Alert을 발송하는 중 알 수 없는 오류가 발생하였습니다. 관리자에게 문의하세요.");
            }
            
            
            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "발송이 성공하였습니다.");
            mResult.put("INSERT_ALERT_CNT", iInsertAlertCnt);
            mResult.put("INSERT_ALERT_TARGET_CNT", iInsertAlertTargetCnt);
            mResult.put("SEND_CNT", iSendCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
}
