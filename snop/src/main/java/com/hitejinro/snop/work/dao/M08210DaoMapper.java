package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > 모제품 매핑
 * @author 손성은
 *
 */
@Repository
public interface M08210DaoMapper {

	/**
	 * 데이터 조회
	 * @param params { liquorCode }
	 * @return [[{LIQUOR_CODE, FR_ITEM_CODE, FR_ITEM_NAME, FR_CONVERSION_BULK, TO_ITEM_CODE, TO_ITEM_NAME, CREATION_DATE, LAST_UPDATE_DATE, CREATED_BY, LAST_UPDATED_BY, RMKS, BRAND_NAME, VESSEL_SORT, VOLUME_VALUE, CONVERSION_VALUE }]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

	/**
	 * FR제품마스터 조회
	 * @param params { liquorCode }
	 * @return [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME, BRAND_NAME, VESSEL_SORT, VOLUME_VALUE, CONVERSION_BULK, LIQUOR_CODE, LIQUOR_CODE_NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchItemList(Map<String, Object> params) throws Exception;

	/**
	 * 데이터 저장 : 유효성 검증
	 * @param params { updateData : [{ updateList }]}
	 * @return [{ ERR_CNT, ERR_MSG }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> validate(Map<String, Object> params) throws Exception;

	/**
	 * 데이터 저장 : 추가/수정
	 * @param params { userId, updateList }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;

	/**
	 * 데이터 저장 : 삭제
	 * @param params { userId, deleteList }
	 * @return
	 * @throws Exception
	 */
	public int delete(Map<String, Object> params) throws Exception;

	/**
	 * 이력관리 테이블에 CRUD된 데이터 저장
	 * @param { row }
	 * @throws Exception
	 */
	public void insertHistory(Map<String, Object> row) throws Exception;

	/**
	 * 이중매핑된 로우 찾기
	 * @return { row }
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchConversion(Map<String, Object> params) throws Exception;

	/**
	 * 데이터 저장 : 이중매핑된 로우 대상으로 매핑 수정
	 * @param { userId, { row } }
	 * @throws Exception
	 */
	public void updateConversion(Map<String, Object> params) throws Exception;

}
