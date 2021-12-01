package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M01010DaoMapper;

/**
 * 프로그램 :: M01010 : 실시간 판매현황(당일 판매예측)
 * 작성일자 :: 2021.06.29
 * 작 성 자 :: 김태환
 */
@Service
public class M01010Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M01010Service.class);

	@Inject
	private M01010DaoMapper m08110DaoMapper;

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> search(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(m08110DaoMapper.search(params));

		return list;
	}

	/**
	 * 기간동안의 일요일을 제외한 날짜 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDateGrid(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		list = m08110DaoMapper.searchDateGrid(params);

		return list;
	}

}
