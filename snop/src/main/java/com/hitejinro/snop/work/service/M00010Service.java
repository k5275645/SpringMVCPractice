package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M00010DaoMapper;

/**
 * 프로그램 :: M00010 : 초기화면
 * 작성일자 :: 2021.7.27
 * 작 성 자 :: 김태환
 */
@Service
public class M00010Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M00010Service.class);

	@Inject
	private M00010DaoMapper M00010DaoMapper;

	/**
	 * 제품수급_판매(맥주/소주 공통)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSalesGrid(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M00010DaoMapper.searchSalesGrid(params);

		return list;
	}

	/**
	 * 제품수급_생산(맥주/소주 공통)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPrdtGrid(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M00010DaoMapper.searchPrdtGrid(params);

		return list;
	}

	/**
	 * 제품수급_재고(맥주/소주 공통)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStockGrid(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M00010DaoMapper.searchStockGrid(params);

		return list;
	}

	/**
	 * 판매구성비 파이차트(맥주/소주 공통사용)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSalePie(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M00010DaoMapper.searchSalePie(params);

		return list;
	}

	/**
	 * 재고구성비 파이차트(맥주/소주 공통사용)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStockPie(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M00010DaoMapper.searchStockPie(params);

		return list;
	}

	/**
	 * 용기수급
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchVesselGrid(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = M00010DaoMapper.searchVesselGrid(params);

		return list;
	}

}
