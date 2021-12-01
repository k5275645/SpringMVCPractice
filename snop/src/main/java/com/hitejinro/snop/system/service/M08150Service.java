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
import com.hitejinro.snop.system.dao.M08150DaoMapper;

/**
 * 권한 관리
 * @author 김남현
 *
 */
@Service
public class M08150Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08150Service.class);

	@Inject
	private M08150DaoMapper m08150DaoMapper;

	/**
	 * 권한 조회
	 * @param params { useYN }
	 * @return [[{AUTH_CD, AUTH_NM, AUTH_DESC...}]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m08150DaoMapper.search(params);
	}

	/**
	 * 권한 저장
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

			if (Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}

		// 추가/수정/삭제
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));

			// validation
			List<Map<String, Object>> errorList = m08150DaoMapper.validate(updateData);

			if (errorList != null && errorList.size() > 0) {
				StringBuilder error = new StringBuilder();
				error.append("[권한 관리]에 오류\r\n");

				for (Map<String, Object> row : errorList) {
					error.append(row.get("ERR_MSG"));
					error.append("\r\n");
				}

				throw new UserException(error.toString());
			}

			// 권한 추가/수정
			m08150DaoMapper.update(updateData);
			// 권한/사용자 mapping 삭제
			m08150DaoMapper.deleteAuthUser(updateData);
			// 권한/메뉴 mapping 삭제
			m08150DaoMapper.deleteAuthMenu(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");

		return result;
	}
	
	/**
	 * 권한콤보 조회
	 * @param params { authCd, authNm }
	 * @return [[{ AUTH_CD, AUTH_NM }]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAuth(Map<String, Object> params) throws Exception {
		return m08150DaoMapper.getAuth(params);
	}

	/**
	 * 권한/메뉴 조회
	 * @param params { authCd }
	 * @return [[{MENU_CD, MENU_NM, MENU_DESC...}]]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetail(Map<String, Object> params) throws Exception {
		return m08150DaoMapper.searchDetail(params);
	}
	
	/**
	 * 권한/메뉴 저장
	 * @param params { saveData, authCd, userId }
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

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);

			if (Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}

		// 추가/수정/삭제
		if (updateList.size() > 0) {

			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("authCd", params.get("authCd"));
			updateData.put("userId", params.get("userId"));

			// 삭제
			m08150DaoMapper.deleteDetail(updateData);

			// 추가/수정
			m08150DaoMapper.updateDetail(updateData);

		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;

	}

}
