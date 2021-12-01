package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M09030DaoMapper;

/**
 * 반제품 생산실적
 * @author 남동희
 *
 */
@Service
public class M09030Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M09030Service.class);

	@Inject
	private M09030DaoMapper m09030DaoMapper;

	/**
	 * 헤더 조회
	 * @param params {YYYYMM, startDate, endDate}
	 * @return [{NAME, VISIBLE, DEF, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception {
		return m09030DaoMapper.searchHeader(params);
	}
	
	/**
	 * 조회
	 * @param params {YYYYMM, startDate, endDate, liquorCode, mfgCode, itemCode, unit}
	 * @return [{MFG_CODE, ITEM_CODE, MFG_NAME, DESCRIPTION, SEGMENT3, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m09030DaoMapper.search(params);
	}
}
