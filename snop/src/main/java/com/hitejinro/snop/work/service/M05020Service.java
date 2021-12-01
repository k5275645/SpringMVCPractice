package com.hitejinro.snop.work.service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hitejinro.snop.common.util.NumberUtil;
import com.hitejinro.snop.work.dao.M05020DaoMapper;

/**
 * 프로그램 :: M05020 : 선도관리 현황
 * 작성일자 :: 2021.8.04
 * 작 성 자 :: 김태환
 */
@Service
public class M05020Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M05020Service.class);

	@Inject
	private M05020DaoMapper M05020DaoMapper;

	/**
	 * 가변날짜조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchYearMonthGrid(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		list = M05020DaoMapper.searchYearMonthGrid(params);

		return list;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> search(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(M05020DaoMapper.search(params));

		return list;
	}

	/**
	 * 차수 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> yyyywwCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M05020DaoMapper.yyyywwCombo(params);

		return list;
	}

	/**
	 * 조직(센터/공장) Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> centerCodeCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M05020DaoMapper.centerCodeCombo(params);

		return list;
	}

	/**
	 * 팝업조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> searchPop(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(M05020DaoMapper.searchPop(params));

		return list;
	}

}
