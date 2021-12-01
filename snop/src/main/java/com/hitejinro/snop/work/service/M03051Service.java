package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03051DaoMapper;

/**
 * 공병 생산 지표
 * @author 남동희
 *
 */
@Service
public class M03051Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03051Service.class);

	@Inject
	private M03051DaoMapper m03051DaoMapper;
	
	/**
	 * 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, acctType, liquorCode, mfgCode, volumeValue, brandCode }
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, VOLUME_VALUE, BRAND_CODE, MFG_CODE, NEW_BOTL_RATE, PRDT_QTY...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {

		if ("YEARLY".equals(params.get("searchType"))) {
			return m03051DaoMapper.searchYearly(params);
		} else if ("MONTHLY".equals(params.get("searchType"))) {
			return m03051DaoMapper.searchMonthly(params);
		} else if ("DAILY".equals(params.get("searchType"))) {
			return m03051DaoMapper.searchDaily(params);
		} else {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

	}
}
