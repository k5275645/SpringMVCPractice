package com.hitejinro.snop.system.service;

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
import com.hitejinro.snop.system.dao.M08180DaoMapper;

/**
 * 공통코드 관리
 * @author 김남현
 *
 */
@Service
public class M08180Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08180Service.class);

	@Inject
	private M08180DaoMapper m08180DaoMapper;
	
	/**
	 * 공통그룹 조회
	 * @param params { groupCode, useYN }
	 * @return [[{GROUP_CODE, GROUP_NAME...}]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m08180DaoMapper.search(params);
	}
	
	/**
	 * 공통그룹 저장
	 * @param params { saveData, userId }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		
		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
		
		// action 할당
		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);
			
			if(Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}
		
		// 추가, 수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));
			
			// validation
			List<Map<String, Object>> errorList = m08180DaoMapper.validate(updateData);
			
			if (errorList != null && errorList.size() > 0) {
				StringBuilder error = new StringBuilder();
				error.append("[공통그룹 관리]에 오류\r\n");
				
				for (Map<String, Object> row : errorList) {
					error.append(row.get("ERR_MSG"));
					error.append("\r\n");
				}
				
				throw new UserException(error.toString());
			}
			
			m08180DaoMapper.update(updateData);
			// 상위 코드 USE_YN = 'N' -> 하위코드 모두 USE_YN ='N' 
			m08180DaoMapper.updateDetailUseYN(updateData);
		}
			
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");
		
		return result;
	}
	
	/**
	 * 공통코드 조회
	 * @param params { groupCode, useYN }
	 * @return [[{GROUP_CODE, CODE, NAME...}]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetail(Map<String, Object> params) throws Exception {
		return m08180DaoMapper.searchDetail(params);
	}
	
	/**
	 * 공통코드 저장 
	 * @param params { saveData, userId }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveDetail(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		
		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
		
		// action 할당
		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);
			
			if(Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}
		
		// 추가, 수정 Detail
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));
			
			// validationDetail
			List<Map<String, Object>> errorList = m08180DaoMapper.validateDetail(updateData);
			
			if (errorList != null && errorList.size() > 0) {
				StringBuilder error = new StringBuilder();
				error.append("[공통코드 관리]에 오류\r\n");
				
				for (Map<String, Object> row : errorList) {
					error.append(row.get("ERR_MSG"));
					error.append("\r\n");
				}
				
				throw new UserException(error.toString());
			}
			
			m08180DaoMapper.updateDetail(updateData);
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");
		
		return result;
	}
	
}
