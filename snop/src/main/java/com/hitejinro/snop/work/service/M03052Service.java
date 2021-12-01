package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03052DaoMapper;

/**
 * 용기 지표 구매폐기
 * @author 남동희
 *
 */
@Service
public class M03052Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03052Service.class);

	@Inject
	private M03052DaoMapper m03052DaoMapper;

	/**
	 * 조회
	 * @param params {startYYYYMM, endYYYYMM, liquorCode, acctType, volume, itemCode, header : [{CODE, NAME}]} 
	 * @return [{PERIOD_YYYYMMDD, LIQUOR_CODE, ITEM_CODE, PBOX_PRDT_QTY, BULK_PRDT_QTY, PRDT_QTY, SHIP_QTY, ..., PBOX_STOCK_QTY, BULK_STOCK_QTY}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {

		if ("YEARLY".equals(params.get("searchType"))) {
			return m03052DaoMapper.searchYearly(params);
		} else if ("MONTHLY".equals(params.get("searchType"))) {
			return m03052DaoMapper.searchMonthly(params);
		} else {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

	}
}
