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
import com.hitejinro.snop.work.dao.M07050DaoMapper;

/**
 * KPI > 제품재고일수
 * @author 이수헌
 *
 */
@Service
public class M07050Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07050Service.class);

	@Inject
	private M07050DaoMapper m07050DaoMapper;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> search(Map<String, Object> params) throws Exception {
		if(params == null) params = new HashMap<String, Object>();
		
		// 1. Header 조회 : 조회기간에 따른 컬럼 구성 리스트 : [{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC }]
        params.put("TREEGRID_HEADER", m07050DaoMapper.searchHeader(params));
        
        // 2. Body 조회 : 헤더를 이용해서, 가변 컬럼 데이터 조회 :
        params.put("TREEGRID_BODY", m07050DaoMapper.searchBody(params));
        
		return params;
	}
	
	/**
	 * 팝업 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchPop(Map<String, Object> params) throws Exception {
		if(params == null) params = new HashMap<String, Object>();
		
		// 1. Header 조회 : 조회기간에 따른 컬럼 구성 리스트 : [{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC }]
        params.put("TREEGRID_HEADER", m07050DaoMapper.searchPopHeader(params));
        
        // 2. Body 조회 : 헤더를 이용해서, 가변 컬럼 데이터 조회 :
        params.put("TREEGRID_BODY", m07050DaoMapper.searchPopBody(params));
        
		return params;
	}
	
	/**
	 * 마감년월 조회
	 * @return
	 * @throws Exception
	 */
	public String getMagamYmd() throws Exception {
		return m07050DaoMapper.getMagamYmd();
	}
	
	/**
	 * Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m07050DaoMapper.getCombo(params);

		return list;
	}

}
