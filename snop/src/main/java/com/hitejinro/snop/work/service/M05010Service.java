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
import com.hitejinro.snop.work.dao.M05010DaoMapper;

/**
 * 프로그램 :: M05010 : 체화재고 현황
 * 작성일자 :: 2021.7.27
 * 작 성 자 :: 김태환
 */
@Service
public class M05010Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M05010Service.class);

	@Inject
	private M05010DaoMapper M05010DaoMapper;

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> search(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(M05010DaoMapper.search(params));

		return list;
	}

	/**
	 * 상단 요약정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> summaryResult(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M05010DaoMapper.summaryResult(params);

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
		list = M05010DaoMapper.yyyywwCombo(params);

		return list;
	}

	/**
	 * 지점 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> deptCodeCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M05010DaoMapper.deptCodeCombo(params);

		return list;
	}

	/**
	 * 센터 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> centerCodeCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M05010DaoMapper.centerCodeCombo(params);

		return list;
	}

	/**
	 * 년, 월, 주차 기본설정(최종입력주차) 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> defaultYearMonth(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M05010DaoMapper.defaultYearMonth(params);

		return list;
	}

	/**
	 * 마감 기준일자 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchMagamStr(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M05010DaoMapper.searchMagamStr(params);

		return list;
	}

}
