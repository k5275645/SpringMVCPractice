package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M08230DaoMapper;

/**
 * 기준정보 > 사용자 로그 조회
 * @author 유기후
 *
 */
@Service
public class M08230Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08230Service.class);

    @Inject
    private M08230DaoMapper m08230DaoMapper;
    
    /**
     * 데이터 조회
     * @param params { frYYYYMMDD, toYYYYMMDD }
     * @return [{ USER_LOG_SEQNO, SERVER_MODE, YYYYMMDD, LOG_DATE, MENU_CD, MENU_NM, MENU_URL, USER_ID, USER_NM, EMPLOYEE_NUMBER, DEPT_CD, DEPT_NM, POSITION_CODE, POSITION_NAME, DUTY_CODE, DUTY_NAME, AUTH_CD, AUTH_NM, CLIENT_IP, URL }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m08230DaoMapper.search(params);
        return list;
    }
    
}
