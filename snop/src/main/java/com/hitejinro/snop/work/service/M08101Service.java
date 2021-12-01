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
import com.hitejinro.snop.work.dao.M08101DaoMapper;

/**
 * 주단위 생산 CAPA 설정
 * @author 김남현
 *
 */
@Service
public class M08101Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08101Service.class);

	@Inject
	private M08101DaoMapper m08101DaoMapper;
	
	/**
	 * 주단위 생산 CAPA 설정 > 헤더 조회
	 * @param params { liquorCd } 
	 * @return [{CODE, NAME, HEADER1_DESC, HEADER1_SPAN...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception {
		return m08101DaoMapper.searchHeader(params);
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 조회
	 * @param params { liquorCd }
	 * @return [{WEEK_WORK_DCNT_TP_CODE, LIQUOR_CODE...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m08101DaoMapper.search(params);
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 저장
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

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);

			if (Const.ROW_STATUS_UPDATE.equals(action)) {
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
			
			List<Map<String, Object>> errorList = m08101DaoMapper.validate(updateData);
			
			if (errorList != null && errorList.size() >0) {
				StringBuilder error = new StringBuilder();
				error.append("[주단위 생산 CAPA 설정]에 오류 \r\n");
				
				for (Map<String, Object> row : errorList) {
					error.append(row.get("ERR_MSG"));
					error.append("\r\n");
				}
				
				throw new UserException(error.toString());
			}
			
			m08101DaoMapper.update(updateData);
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 팝업 > 조회
	 * @param params {} 
	 * @return [{CODE, NAME, DESCRIPTION...,}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchGroupCd(Map<String, Object> params) throws Exception {
		return m08101DaoMapper.searchGroupCd(params);
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 팝업 > 저장
	 * @param params { saveData, userId }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveGroupCd(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);

			if (Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
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
			
			m08101DaoMapper.updateGroupCd(updateData);
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}

}
