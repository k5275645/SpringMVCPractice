package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M09010DaoMapper;

/**
 * 생산계획 진척률
 * @author 남동희
 *
 */
@Service
public class M09010Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M09010Service.class);

	@Inject
	private M09010DaoMapper m09010DaoMapper;

	/**
	 * 헤더 조회
	 * @param params 
	 * @return { startDate, endDate, endYYYYMM }
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception {
		return m09010DaoMapper.searchHeader(params);
	}

	/**
	 * 조회
	 * @param params { startDate, endDate, liquorCode, mfgCode, acctType, unit, segment2, segment3, brandCode, usageCode, vesselCode, volumeValue, domExpCode, mainFlag, eventItemFlag }
	 * @return 
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchGrid(Map<String, Object> params) throws Exception {
		return m09010DaoMapper.searchGrid(params);
	}

	public List<Map<String, Object>> searchWeeklyChart(Map<String, Object> params) throws Exception {
		return m09010DaoMapper.searchWeeklyChart(params);
	}
	
	public List<Map<String, Object>> searchMonthlyChart(Map<String, Object> params) throws Exception {
		return m09010DaoMapper.searchMonthlyChart(params);
	}
}
