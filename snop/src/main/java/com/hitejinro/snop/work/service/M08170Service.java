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
import org.springframework.util.StringUtils;

import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M08170DaoMapper;

/**
 * 제품적재규모(공장, 물류센터)
 * @author 김남현
 *
 */
@Service
public class M08170Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08170Service.class);

	@Inject
	private M08170DaoMapper m08170DaoMapper;

	/**
	 * 제품적재 규모 버전리스트 콤보 조회
	 * @param params { verCd }
	 * @return [{ ORG_CAPA_VER_CD, ORG_CAPA_VER_NM, CREATION_DATE }, ] 
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVersion(Map<String, Object> params) throws Exception {
		return m08170DaoMapper.getVersion(params);
	}

	/**
	 * 제품적재규모(공장, 물류센터) 조회
	 * @param params { verCd, useYn }
	 * @return { Body : [[{ORG_CODE, ORG_NAME, ORG_WH_OPT_CAPA...}]], _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m08170DaoMapper.search(params);
	}

	/**
	 * 제품적재규모(공장, 물류센터) 저장
	 * @param params { saveData, verCd }
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

		// 수정/삭제/추가
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));
			updateData.put("verCd", params.get("verCd"));

			List<Map<String, Object>> errorList = m08170DaoMapper.validateUpdate(updateData);

			if (errorList != null && errorList.size() > 0) {
				StringBuilder error = new StringBuilder();
				error.append("[제품적재 규모(공장,물류센터)\r\n]");

				for (Map<String, Object> row : errorList) {
					error.append(row.get("ERR_MSG"));
					error.append("\r\n");
				}

				throw new UserException(error.toString());
			}

			m08170DaoMapper.update(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}

	/**
	 * 제품적재규모(공장, 물류센터) 버전추가
	 * @param params { verNm, YYYYMM }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	public Map<String, Object> addVersion(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> version = m08170DaoMapper.getNewVersion(params);
		params.put("lastVerCd", version.get("LAST_VER_CD"));
		params.put("newVerCd", version.get("NEW_VER_CD"));

		Map<String, Object> error = m08170DaoMapper.validateNewVersion(params);

		if (error != null && !StringUtils.isEmpty(error.get("ERR_MSG"))) {
			throw new UserException((String) error.get("ERR_MSG"));
		}

		// 버전추가
		m08170DaoMapper.insertNewVersion(params);

		// 버전상세추가
		m08170DaoMapper.insertNewVersionDetail(params);

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
}
