package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > 사용자 로그 조회
 * @author 유기후
 *
 */
@Repository
public interface M08230DaoMapper {
    
    /**
     * 데이터 조회
     * @param params { frYYYYMMDD, toYYYYMMDD }
     * @return [{ USER_LOG_SEQNO, SERVER_MODE, YYYYMMDD, LOG_DATE, MENU_CD, MENU_NM, MENU_URL, USER_ID, USER_NM, EMPLOYEE_NUMBER, DEPT_CD, DEPT_NM, POSITION_CODE, POSITION_NAME, DUTY_CODE, DUTY_NAME, AUTH_CD, AUTH_NM, CLIENT_IP, URL }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
    
}
