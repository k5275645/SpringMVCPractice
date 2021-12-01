package com.hitejinro.snop.work.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M09020DaoMapper;

/**
 * 생산관리 > 공장 생산출고재고
 * @author 유기후
 *
 */
@Service
public class M09020Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M09020Service.class);

    @Inject
    private M09020DaoMapper m09020DaoMapper;

    
    /**
     * 최신 마감년월 조회
     * @param params {  }
     * @return [{ MAX_YYYYMM }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectMaxPeriod(Map<String, Object> params) throws Exception {
        return m09020DaoMapper.selectMaxPeriod(params);
    }
    
    
    /**
     * 데이터 조회 : Header, Body 둘다 조회
     * @param params { frYYYYMM, toYYYYMM, liquorCode, orgCode, itemSegment2, itemSegment3, brandCode, usageCode, vesselCode, domExpCode, mainFlag, eventItemFlag }] }
     * @return { TREEGRID_HEADER[{  }], TREEGRID_BODY[{  }], CHART_DATA[{  }] }
     * @throws Exception
     */
    public Map<String, Object> search(Map<String, Object> params) throws Exception {
        if(params == null) params = new HashMap<String, Object>();
        
        // 1. Header 조회 : 조회기간에 따른 컬럼 구성 리스트 : [{ PERIOD_CODE, PERIOD_NAME, PERIOD_SEQ, FR_YYYYMM, TO_YYYYMM, ACCT_CD, ACCT_NM, ACCT_VISIBLE, ACCT_SEQ, DATA_INFO, COL_ID, HEADER_COL_SPAN }]
        params.put("TREEGRID_HEADER", m09020DaoMapper.searchHeader(params));
        
        // 2. Body 조회 : 헤더를 이용해서, 가변 컬럼 데이터 조회 :
        params.put("TREEGRID_BODY", m09020DaoMapper.searchBody(params));
        
        // 3. 차트 조회
        params.put("CHART_DATA", m09020DaoMapper.searchChart(params));
        
        return params;
    }
}
