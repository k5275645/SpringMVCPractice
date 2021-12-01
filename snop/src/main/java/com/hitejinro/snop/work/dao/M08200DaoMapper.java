package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > Alert 기능 종합
 * @author 유기후
 *
 */
@Repository
public interface M08200DaoMapper {
    
    /**
     * 데이터 조회 : Alert 리스트
     * @param params {  }
     * @return [{ ALERT_CODE, ALERT_NAME, ALERT_DESC, ALERT_TYPE, SEND_PERIOD_CODE, SEND_HR, SEND_TYPE, USE_YN, SEND_DEPT_TYPE, PROGRAM_NAME, MSG, RMKS, TARGET_CNT, CODE, NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

    /**
     * 데이터 저장 : Alert 리스트 수정
     * @param params { ALERT_CODE, ALERT_NAME, ALERT_DESC, USE_YN, userId }
     * @return
     * @throws Exception
     */
    public int update(Map<String, Object> params) throws Exception;
    
    /**
     * 체화재고- 전사관점 호출 : SCMU.PKG_SOP_ALERT.SP_SEND_ACCUM_STOCK_OU
     * @param params { userId, alertCode, O_TOT_ROWCOUNT, O_RETURN_MSG, O_RETURN_STATUS }
     * @return
     * @throws Exception
     */
    public void callAlertSendAccumStockOu(Map<String, Object> params) throws Exception;
    
    /**
     * 체화재고- 지점관점 호출 : SCMU.PKG_SOP_ALERT.SP_SEND_ACCUM_STOCK_DEPARTMENT
     * @param params { userId, alertCode, O_TOT_ROWCOUNT, O_RETURN_MSG, O_RETURN_STATUS }
     * @return
     * @throws Exception
     */
    public void callAlertSendAccumStockDepartment(Map<String, Object> params) throws Exception;
    
    
    
    /**
     * 사용자 리스트 조회
     * @param params {  }
     * @return [{ CODE, NAME, EMPLOYEE_NUMBER, EMPLOYEE_KOR_NAME, DUTY_CODE, DUTY_NAME, POSITION_CODE, POSITION_NAME, DEPARTMENT_CODE, DEPARTMENT_NAME, DEPT_CODE, DEPARTMENT_NAME_PATH }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectUserList(Map<String, Object> params) throws Exception;

    /**
     * 사용자의 부서 리스트 조회
     * @param params {  }
     * @return [{ DEPARTMENT_CODE, DEPARTMENT_NAME, DEPT_CODE }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectUserDepartmentList(Map<String, Object> params) throws Exception;

    /**
     * 지점 리스트 조회
     * @param params {  }
     * @return [{ DEPT_CODE, DEPT_NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectDeptList(Map<String, Object> params) throws Exception;
    
    

    /**
     * Alert 대상자 데이터 조회
     * @param params { alertCode }
     * @return [{ ALERT_CODE, TARGET_TYPE, TARGET_VAL, SEND_DEPT_TYPE, SEND_DEPT_VAL, USE_YN }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchAlertTarget(Map<String, Object> params) throws Exception;

    /**
     * Alert 대상자 데이터 저장 : 추가
     * @param params { ALERT_CODE, TARGET_TYPE, TARGET_VAL, SEND_DEPT_VAL, USE_YN, userId }
     * @return
     * @throws Exception
     */
    public int insertAlertTarget(Map<String, Object> params) throws Exception;

    /**
     * Alert 대상자 데이터 저장 : 수정
     * @param params { ALERT_CODE, TARGET_TYPE, TARGET_VAL, SEND_DEPT_VAL, USE_YN, userId }
     * @return
     * @throws Exception
     */
    public int updateAlertTarget(Map<String, Object> params) throws Exception;

    /**
     * Alert 대상자 데이터 저장 : 삭제
     * @param params { ALERT_CODE, TARGET_TYPE, TARGET_VAL }
     * @return
     * @throws Exception
     */
    public int deleteAlertTarget(Map<String, Object> params) throws Exception;

    
    
    /**
     * 신규 수동 Alert 코드
     * @param params {  }
     * @return [{ NEW_ALERT_CODE, NEW_DTM }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectNewManualAlertCode(Map<String, Object> params) throws Exception;

    /**
     * 수동 Alert 데이터 저장 : 수동 Alert 추가
     * @param params { NEW_ALERT_CODE, NEW_DTM, msg, userId }
     * @return
     * @throws Exception
     */
    public int insertManualAlert(Map<String, Object> params) throws Exception;

    /**
     * 수동 Alert 발송
     * @param params { NEW_ALERT_CODE }
     * @return
     * @throws Exception
     */
    public int sendManualAlert(Map<String, Object> params) throws Exception;
    
    
}
