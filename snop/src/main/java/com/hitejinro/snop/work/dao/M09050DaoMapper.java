package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 생산관리 > 부적합품 관리
 * @author 김남현
 *
 */
@Repository
public interface M09050DaoMapper {
	
	/**
	 * 부적합품 관리 > 조회
	 * @param params { frYYYYMM, toYYYYMM }
	 * @return [{ NC_NCM_DATE, NC_ORGANIZATION_NAME, NC_ITEM, NC_ITEM_DESC, NC_NONCONFORMANCE_GRADE, NC_NCM_DEPT, NC_SHORT_DESCRIPTION, NC_DETAILED_DESCRIPTION, NC_COMPONENT_ITEM_DESC, NC_DEFECT_CODE_COMMENT, NC_QUANTITY_NONCONFORMING, NC_SUPPLIER, NC_COMMITTEE_COMMENTS, CA_DETAILED_DESCRIPTION, CI_DETAILED_DESCRIPTION }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
		
}
