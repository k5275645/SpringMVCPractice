package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M07010DaoMapper;

/**
 * 프로그램 :: M07010 : 월간 판매계획 준수율
 * 작성일자 :: 2021.09.07
 * 작 성 자 :: 김태환
 */
@Service
public class M07010Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07010Service.class);

	@Inject
	private M07010DaoMapper M07010DaoMapper;

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> search(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(M07010DaoMapper.search(params));

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
		list = M07010DaoMapper.searchUpdtmStr(params);

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
		list.add(M07010DaoMapper.searchSubGrid(params));

		return list;
	}

	/**
	 * Chart 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchChart(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M07010DaoMapper.searchChart(params);

		return list;
	}

}
