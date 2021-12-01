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

import com.hitejinro.snop.common.dao.CommonComboDaoMapper;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03060DaoMapper;

/**
 * 제병사실적 관리(입력)
 * @author 남동희
 *
 */
@Service
public class M03060Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03060Service.class);

	@Inject
	private M03060DaoMapper m03060DaoMapper;

	@Inject
	private CommonComboDaoMapper commonComboDaoMapper;

	/**
	 * 조회
	 * @param params {startDate, endDate, botlManursCode, liquorCode, itemCode}
	 * @return [{PERIOD_YYYYMMDD, BOTL_MANURS_CODE, LIQUOR_CODE, ITEM_CODE, BOTL_PACKING_TYPE, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m03060DaoMapper.search(params);
	}

	/**
	 * 가변 헤더 목록 조회
	 * @param params 
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchVisibleHeader(Map<String, Object> params) throws Exception {
		params.put("groupCode", "BOTL_MANURS_ACCT_LIST");
		params.put("segment6", params.get("liquorCode"));
		return commonComboDaoMapper.getComCodeCombo(params);
	}

	/**
	 * 저장
	 * @param params {uid, saveData : [{action, PERIOD_YYYYMMDD, BOTL_MANURS_CODE, LIQUOR_CODE, ITEM_CODE, BOTL_PACKING_TYPE, ...}]
	 * @return {_RESULT_MSG, _RESULT_FLAG}
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

			if (Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}

		// 추가 및 수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));

			m03060DaoMapper.update(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
}
