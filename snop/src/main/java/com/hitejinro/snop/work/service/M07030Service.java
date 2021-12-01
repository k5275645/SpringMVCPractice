package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M07030DaoMapper;

/**
 * 프로그램 :: M07030 : 본부 판매예측 준수율
 * 작성일자 :: 2021.09.14
 * 작 성 자 :: 김태환
 */
@Service
public class M07030Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07030Service.class);

	@Inject
	private M07030DaoMapper M07030DaoMapper;

	/**
	 * 기간동안의 주차 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDateGrid(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		list = M07030DaoMapper.searchDateGrid(params);

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
		list.add(M07030DaoMapper.search(params));

		return list;
	}

}
