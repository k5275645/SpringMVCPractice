package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M00010 : 초기화면
 * 작성일자 :: 2021.7.27
 * 작 성 자 :: 김태환
 */
@Repository
public interface M00010DaoMapper {
	
	/**
	 * 제품수급_판매(맥주/소주 공통)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSalesGrid(Map<String, Object> params) throws Exception;
	
	/**
	 * 제품수급_생산(맥주/소주 공통)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPrdtGrid(Map<String, Object> params) throws Exception;
	
	/**
	 * 제품수급_재고(맥주/소주 공통)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStockGrid(Map<String, Object> params) throws Exception;
	
	/**
	 * 판매구성비 파이차트(맥주/소주 공통사용)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSalePie(Map<String, Object> params) throws Exception;
	
	/**
	 * 재고구성비 파이차트(맥주/소주 공통사용)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStockPie(Map<String, Object> params) throws Exception;
	
	/**
	 * 용기수급
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchVesselGrid(Map<String, Object> params) throws Exception;
	
}
