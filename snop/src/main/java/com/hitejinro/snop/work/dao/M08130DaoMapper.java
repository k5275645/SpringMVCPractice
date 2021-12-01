package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 판매변수 정의(코로나, 명절, 휴가 등)
 * @author 김남현
 *
 */
@Repository
public interface M08130DaoMapper {
	
	/**
	 * 판매변수 정의(코로나, 명절, 휴가 등) 조회
	 * @param params { year }
	 * @return [{ SALE_VAR_DFNT_SEQNO, VLD_STR_DT, VLD_END_DT... }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 제품 리스트 조회
	 * @param params { LIQUOR_CODE, USAGE_CODE }
	 * @return [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMappingItemList(Map<String, Object> params) throws Exception;
	
	/**
	 * 삭제
	 * @param params { updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int delete(Map<String, Object> parmas) throws Exception;
	
	/**
	 * 유효성 검사
	 * @param params { updateList, userId}
	 * @return [{ SALE_VAR_DFNT_SEQNO, IDX ..번째 행 오류...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> validate(Map<String, Object> params) throws Exception;
	
	/**
	 * 수정
	 * @param params { updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
}
