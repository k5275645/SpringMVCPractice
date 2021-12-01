package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 판매실적분석(품목)
 * @author 이수헌
 *
 */
@Repository
public interface M01031DaoMapper {

	/**
	 * 헤더 조회
	 * @param params  
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;

	/**
	 * 조회
	 * @param params 
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchBody(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getSegment2Combo(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> getSegment3Combo(Map<String, Object> params) throws Exception;

}
