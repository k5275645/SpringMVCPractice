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
import com.hitejinro.snop.work.dao.M07060DaoMapper;

/**
 * KPI > 체화 재고 관리
 * @author 이수헌
 *
 */
@Service
public class M07060Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07060Service.class);

	@Inject
	private M07060DaoMapper m07060DaoMapper;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m07060DaoMapper.search(params);
		
		return list;
	}


	/**
	 * 주차 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getScmYmw(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m07060DaoMapper.getScmYmw(params);

		return list;
	}
	

	
	/**
	 * 팝업 그리드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> searchPop(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(m07060DaoMapper.searchPop(params));

		return list;
	}
}
