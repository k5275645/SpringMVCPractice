package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.dao.CommonComboDaoMapper;
import com.hitejinro.snop.work.dao.M04010DaoMapper;

/**
 * 센터-품목별 재고 현황
 * @author 남동희
 *
 */
@Service
public class M04010Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M04010Service.class);

	@Inject
	private M04010DaoMapper m04010DaoMapper;
	
	@Inject
	private CommonComboDaoMapper commonComboDaoMapper;
	
	/**
	 * 센터-품목별 안전재고 기준 조회
	 * @param params
	 * @return [{CODE, NAME, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStockStd(Map<String, Object> params) throws Exception {
		params.put("groupCode", "CENTER_ITEM_SAFETY_STOCK_STD");
		return commonComboDaoMapper.getComCodeCombo(params);
	}

	/**
	 * 조회
	 * @param params {liquorCode, orgCode, itemCode}
	 * @return [{ORG_CODE, ORG_DESC, ITEM_CODE, DESCRIPTION, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m04010DaoMapper.search(params);
	}
}
