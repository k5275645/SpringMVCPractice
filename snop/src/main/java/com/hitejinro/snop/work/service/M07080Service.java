package com.hitejinro.snop.work.service;



import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M07080DaoMapper;

/**
 * KPI > 신병 사용률
 * @author 이수헌
 *
 */
@Service
public class M07080Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07080Service.class);

	@Inject
	private M07080DaoMapper m07080DaoMapper;

	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> search(Map<String, Object> params) throws Exception {
		
		if(params == null) params = new HashMap<String, Object>();
		
		// 1. Header 조회 : 용기회수 KPI 목표에 따른 컬럼 구성 리스트 
        params.put("TREEGRID_HEADER", m07080DaoMapper.searchHeader(params));
        
        // 2. Param 조회 : 기간에 따른 컬럼 구성 리스트
        params.put("TREEGRID_PARAM", m07080DaoMapper.searchParam(params));
        
        // 3. Body 조회 : Header, Param 을 이용해서, 가변 컬럼 데이터 조회 :
        params.put("TREEGRID_BODY", m07080DaoMapper.searchBody(params));
        
		return params;
	}

	/**
	 * 마감년월 조회
	 * @return
	 * @throws Exception
	 */
	public String getMagamYmd() throws Exception {
		return m07080DaoMapper.getMagamYmd();
	}
	
}
