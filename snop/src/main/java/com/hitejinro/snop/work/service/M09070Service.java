package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M09070DaoMapper;

/**
 * 라인별 생산효율
 * @author 남동희
 *
 */
@Service
public class M09070Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M09070Service.class);

	@Inject
	private M09070DaoMapper m09070daoMapper;

	/**
	 * 헤더 조회
	 * @param params {YYYYMM, startDate, endDate}
	 * @return [{NAME, DEF, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception {
		return m09070daoMapper.searchHeader(params);
	}
	
	/**
	 * 조회
	 * @param params {YYYYMM, startDate, endDate, liquorCode, mfgCode, segment2, segment3, brandCode, usageCode, vesselCode, volumeValue, domExpCode}
	 * @return [{MFG_CODE, DEPT_CODE, BRAND_CODE, ITEM_CODE, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m09070daoMapper.search(params);
	}
}
