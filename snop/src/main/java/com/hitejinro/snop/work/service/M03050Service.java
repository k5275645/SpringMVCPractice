package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03050DaoMapper;

/**
 * 용기 지표(용기현황 Index)
 * @author 남동희
 *
 */
@Service
public class M03050Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03050Service.class);

	@Inject
	private M03050DaoMapper m03050DaoMapper;

	/**
	 * 헤더 조회
	 * @param params {startYYYYMM, endYYYYMM, liquorCode, acctType, volume, itemCode, includeYN}
	 * @return [{HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, HEADER2_EXPAND...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception {
		return m03050DaoMapper.searchHeader(params);
	}
	
	/**
	 * 조회
	 * @param params {startYYYYMM, endYYYYMM, liquorCode, acctType, volume, itemCode, header} 
	 * @return [{LIQUOR_CODE, VESSEL_CODE, VOLUME_VALUE, PERIOD_YYYYMMDD, QTY ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {

		if ("YEARLY".equals(params.get("searchType"))) {
			return m03050DaoMapper.searchYearly(params);
		} else if ("MONTHLY".equals(params.get("searchType"))) {
			return m03050DaoMapper.searchMonthly(params);
		} else if ("DAILY".equals(params.get("searchType"))) {
			return m03050DaoMapper.searchDaily(params);
		} else {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

	}
}
