package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 공 P-BOX/PALLET 관리
 * @author 남동희
 *
 */
@Repository
public interface M08220DaoMapper {
	
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;

	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	public int update(Map<String, Object> params) throws Exception;
}
