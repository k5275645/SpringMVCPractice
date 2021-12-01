package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 용기 시뮬레이션 버전 비교
 * @author 남동희
 *
 */
@Repository
public interface M03071DaoMapper {
	
	public List<Map<String, Object>> searchVerCd(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
}
