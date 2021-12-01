package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * KPI > 신병 사용률
 * @author 이수헌
 *
 */
@Repository
public interface M07080DaoMapper {
	
    /**
     * 데이터 조회 : 그리드 헤더 조회(용기회수 KPI 데이터)
     */
    public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 그리드 파라미터 조회(기간 데이터)
     */
    public List<Map<String, Object>> searchParam(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 그리드 바디 조회
     */
    public List<Map<String, Object>> searchBody(Map<String, Object> params) throws Exception;
 
	/**
	 * 마감년월 조회
	 * @return
	 * @throws Exception
	 */
	public String getMagamYmd() throws Exception;
	
}
