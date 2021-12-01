package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 센터-품목별 재고 현황
 * @author 남동희
 *
 */
@Repository
public interface M04010DaoMapper {
	
	/**
	 * 조회
	 * @param params {liquorCode, orgCode, itemCode}
	 * @return [{ORG_CODE, ORG_DESC, ITEM_CODE, DESCRIPTION, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
}
