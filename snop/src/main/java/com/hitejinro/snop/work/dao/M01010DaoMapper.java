package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M01010 : 실시간 판매현황(당일 판매예측)
 * 작성일자 :: 2021.06.29
 * 작 성 자 :: 김태환
 */
@Repository
public interface M01010DaoMapper {
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 기간동안의 일요일을 제외한 날짜 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDateGrid(Map<String, Object> params) throws Exception;
	
}
