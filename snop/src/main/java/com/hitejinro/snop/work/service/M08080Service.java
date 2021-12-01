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
import com.hitejinro.snop.work.dao.M08080DaoMapper;

/**
 * 기준정보 > 제품 과다/부족 기준
 * @author 김남현
 *
 */
@Service
public class M08080Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08080Service.class);

	@Inject
	private M08080DaoMapper m08080DaoMapper;

	/**
	 * 기준정보 > 제품 과다/부족 기준 > 조회
	 * @param params { yyyymm, liquorCd }
	 * @return { [{ITEM_IGRD_TYPE_CODE, STOCK_STATS_CODE, SEQ...}] }
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m08080DaoMapper.search(params);
	}

	/**
	 * 기준정보 > 제품 과다/부족 기준 > 저장
	 * @param params { saveData }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);
			row.put("userId", params.get("userId"));

			if (Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else if (Const.ROW_STATUS_DELETE.equals(action)) {
				deleteList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}

		// 삭제
		for (Map<String, Object> row : deleteList) {
			m08080DaoMapper.delete(row);
		}
		
		for (Map<String, Object> row : updateList) {
			m08080DaoMapper.update(row);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 버전콤보조회
	 * @param params {}
	 * @return { [{CODE, NAME}] }
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVerList(Map<String, Object> params) throws Exception {
		return m08080DaoMapper.getVerList(params);
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 버전복사
	 * @param params { yyyymm }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> versionCopy(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (params == null || params.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
		
		m08080DaoMapper.deleteNowVer(params);
		m08080DaoMapper.copyLastVer(params);

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 팝업창 검색
	 * @param params { searchWord, yyyymm, liquorCd }
	 * @return [{ ITEM_CODE, DESCRIPTION }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPop(Map<String, Object> params) throws Exception {
		return m08080DaoMapper.searchPop(params);
	}

}
