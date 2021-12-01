package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03061DaoMapper;

/**
 * 제병사실적 관리(입력)
 * @author 남동희
 *
 */
@Service
public class M03061Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03061Service.class);

	@Inject
	private M03061DaoMapper m03061DaoMapper;

	/**
	 * 헤더 조회(제병사 목록)
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, liquorCode, acctType, volumeValue, itemCode}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception {
		return m03061DaoMapper.searchHeader(params);
	}

	/**
	 * 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, liquorCode, acctType, volumeValue, itemCode, headerList : [{CODE, NAME]}
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, ITEM_CODE, VOLUME_VALUE, QTY, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {

		if ("YEARLY".equals(params.get("searchType"))) {
			return m03061DaoMapper.searchYearly(params);
		} else if ("MONTHLY".equals(params.get("searchType"))) {
			return m03061DaoMapper.searchMonthly(params);
		} else if ("DAILY".equals(params.get("searchType"))) {
			return m03061DaoMapper.searchDaily(params);
		} else {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
	}

}
