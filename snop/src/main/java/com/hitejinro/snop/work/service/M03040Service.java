package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03040DaoMapper;

/**
 * 용기수급현황(월보)
 * @author 남동희
 *
 */
@Service
public class M03040Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03040Service.class);

	@Inject
	private M03040DaoMapper m03040DaoMapper;

	/**
	 * 공병 - 생산 조회
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, TOTAL_PRDT_QTY, EXCLUDE_NEW_BOTL_QTY, INCLUDE_NEW_BOTL_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPrdt(Map<String, Object> params) throws Exception {
		if ("YEARLY".equals(params.get("searchType"))) {
			return m03040DaoMapper.searchYearlyPrdt(params);
		} else if ("MONTHLY".equals(params.get("searchType"))) {
			return m03040DaoMapper.searchMonthlyPrdt(params);
		} else if ("PERIOD".equals(params.get("searchType"))) {
			return m03040DaoMapper.searchPeriodPrdt(params);
		} else {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
	}

	/**
	 * 공병 - 회수 조회
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return [{PERIOD_YYYYMMDD, ACTUAL_QTY, INCLUDE_RETURN_QTY, EXCLUDE_RETURN_QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchReturn(Map<String, Object> params) throws Exception {
		if ("YEARLY".equals(params.get("searchType"))) {
			return m03040DaoMapper.searchYearlyReturn(params);
		} else if ("MONTHLY".equals(params.get("searchType"))) {
			return m03040DaoMapper.searchMonthlyReturn(params);
		} else if ("PERIOD".equals(params.get("searchType"))) {
			return m03040DaoMapper.searchPeriodReturn(params);
		} else {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
	}

}
