package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M07020DaoMapper;

/**
 * 프로그램 :: M07020 :  주간 판매계획 준수율
 * 작성일자 :: 2021.09.07
 * 작 성 자 :: 김태환
 */
@Service
public class M07020Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07020Service.class);

	@Inject
	private M07020DaoMapper M07020DaoMapper;

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> search(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(M07020DaoMapper.search(params));

		return list;
	}

	/**
	 * 최종 갱신일자 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchUpdtmStr(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M07020DaoMapper.searchUpdtmStr(params);

		return list;
	}

	/**
	 * 상세 그리드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> searchSubGrid(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(M07020DaoMapper.searchSubGrid(params));

		return list;
	}

	/**
	 * 월별 Chart 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchChart(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M07020DaoMapper.searchChart(params);

		return list;
	}

	/**
	 * 주차별 Chart 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchWeekChart(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M07020DaoMapper.searchWeekChart(params);

		return list;
	}

	/**
	 * yyyyww Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> yyyywwCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M07020DaoMapper.yyyywwCombo(params);

		return list;
	}

}
