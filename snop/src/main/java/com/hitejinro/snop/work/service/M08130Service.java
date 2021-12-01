package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M08130DaoMapper;

/**
 * 판매변수 정의(코로나, 명절, 휴가 등)
 * @author 김남현
 *
 */
@Service
public class M08130Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08130Service.class);

	@Inject
	private M08130DaoMapper m08130DaoMapper;
	
	/**
	 * 판매변수 정의(코로나, 명절, 휴가 등) 조회
	 * @param params { year }
	 * @return [{ SALE_VAR_DFNT_SEQNO, VLD_STR_DT, VLD_END_DT... }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m08130DaoMapper.search(params);
	}
	
	/**
	 * 제품 리스트 조회
	 * @param params { LIQUOR_CODE, USAGE_CODE }
	 * @return [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMappingItemList(Map<String, Object> params) throws Exception {
		return m08130DaoMapper.getMappingItemList(params);
	}
	
	/**
	 * 저장
	 * @param params { saveData, userId }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class}, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
		
		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
		
		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);
			
			if (Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else if (Const.ROW_STATUS_DELETE.equals(action)) {
				deleteList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}
		
		// 삭제
		if (deleteList.size() > 0) {
			Map<String, Object> deleteData = new HashMap<String, Object>();
			deleteData.put("deleteList", deleteList);
			m08130DaoMapper.delete(deleteData);
		}
		
		// 추가/수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));
			
			// validation
			List<Map<String, Object>> errorList = m08130DaoMapper.validate(updateData);
			
			if (errorList != null && errorList.size() > 0 ) {
				StringBuilder error = new StringBuilder();
				error.append("[판매 변수 정의(코로나, 명절, 휴가 등)]에 오류\r\n");
				
				for (Map<String, Object> row : errorList) {
					error.append(row.get("ERR_MSG"));
					error.append("\r\n");
				}
				
				throw new UserException(error.toString());
			}
			
			m08130DaoMapper.update(updateData);
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");
		
		return result;
	}

}
