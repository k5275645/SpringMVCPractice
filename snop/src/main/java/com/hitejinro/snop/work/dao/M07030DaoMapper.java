package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M07030 : 본부 판매예측 준수율
 * 작성일자 :: 2021.09.14
 * 작 성 자 :: 김태환
 */
@Repository
public interface M07030DaoMapper {
	
	/**
	 * 기간동안의 주차 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDateGrid(Map<String, Object> params) throws Exception;
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
}
