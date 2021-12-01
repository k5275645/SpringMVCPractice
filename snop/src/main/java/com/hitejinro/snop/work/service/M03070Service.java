package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitejinro.snop.common.dao.CommonComboDaoMapper;
import com.hitejinro.snop.common.dao.CommonDaoMapper;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03070DaoMapper;

/**
 * 용기 공급계획 수립
 * @author 남동희
 *
 */
@Service
public class M03070Service {

	@Inject
	private M03070DaoMapper m03070DaoMapper;

	@Inject
	private CommonDaoMapper commonDaoMapper;

	@Inject
	private CommonComboDaoMapper commonComboDaoMapper;

	/**
	 * 브랜드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchBrand(Map<String, Object> params) throws Exception {
		params.put("liquorCode", "20".split(","));
		params.put("vesselCode", "1".split(","));
		params.put("volumeValue", "360".split(","));
		params.put("botlType", "NEW");
		params.put("companyType", "OWNER");

		return commonDaoMapper.getVesselBrand(params);
	}

	/**
	 * 용기 시뮬레이션 기준 코드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSimulStd(Map<String, Object> params) throws Exception {
		params.put("groupCode", "VESSEL_SIMUL_STD_LIST");
		return commonComboDaoMapper.getComCodeCombo(params);
	}

	/**
	 * 용기 시뮬레이션 기준값 생성 유형 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStdValCrtType(Map<String, Object> params) throws Exception {
		params.put("groupCode", "VESSEL_SIMUL_STD_VAL_CRT_TYPE");
		return commonComboDaoMapper.getComCodeCombo(params);
	}

	/**
	 * 실적기준 계산유형 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchActualCalcType(Map<String, Object> params) throws Exception {
		params.put("groupCode", "ACTUAL_CALC_TYPE");
		return commonComboDaoMapper.getComCodeCombo(params);
	}

	/**
	 * 요일배분 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchActualRfltType(Map<String, Object> params) throws Exception {
		params.put("groupCode", "ACTUAL_RFLT_TYPE_LIST");
		return commonComboDaoMapper.getComCodeCombo(params);
	}

	/**
	 * 조회 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m03070DaoMapper.search(params);
	}

	/**
	 * 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		// action 할당
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

		if (deleteList.size() > 0) {
			Map<String, Object> deleteData = new HashMap<String, Object>();
			deleteData.put("deleteList", deleteList);

			m03070DaoMapper.delete(deleteData);
			m03070DaoMapper.deleteAllStd(deleteData);
			m03070DaoMapper.deleteAllPrdt(deleteData);
			m03070DaoMapper.deleteAllResult(deleteData);
		}

		// 추가/수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));
			updateData.put("YYYYMM", params.get("YYYYMM"));

			m03070DaoMapper.update(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");

		return result;
	}

	/**
	 * 선택된 버전의 정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getVerInfo(Map<String, Object> params) throws Exception {
		return m03070DaoMapper.getVerInfo(params);
	}

	/**
	 * 일일 제품수급 시뮬레이션 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDalyScmSimulVerCd(Map<String, Object> params) throws Exception {
		return m03070DaoMapper.searchDalyScmSimulVerCd(params);
	}

	/**
	 * 일일 제품수급 시뮬레이션 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	public Map<String, Object> saveDalyScmSimulVerCd(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		String verCd = (String) params.get("verCd");
		String dalyScmSimulVerCd = (String) params.get("dalyScmSimulVerCd");

		if (verCd == null || "".equals(verCd)) {
			throw new UserException("용기 시뮬레이션 버전 정보가 유효하지 않습니다.");
		}

		if (dalyScmSimulVerCd == null || "".equals(dalyScmSimulVerCd)) {
			throw new UserException("저장할 제품수급 시뮬레이션 버전 정보가 유효하지 않습니다.");
		}

		int count = m03070DaoMapper.updateDalyScmSimulVerCd(params);

		if (count == 0) {
			throw new UserException("저장 중 문제가 발생하였습니다.");
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");
		return result;
	}

	/**
	 * 기준정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStd(Map<String, Object> params) throws Exception {
		return m03070DaoMapper.searchStd(params);
	}

	/**
	 * 기준정보 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveStd(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		// action 할당
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

		if (deleteList.size() > 0) {
			Map<String, Object> deleteData = new HashMap<String, Object>();
			deleteData.put("deleteList", deleteList);

			m03070DaoMapper.deleteStd(deleteData);
		}

		// 추가/수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("verCd", params.get("verCd"));
			updateData.put("userId", params.get("userId"));

			m03070DaoMapper.updateStd(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");

		return result;
	}

	/**
	 * 제병사 생산계획 헤더 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPrdtHeader(Map<String, Object> params) throws Exception {
		params.put("groupCode", "BOTL_MANURS_LIST");
		return commonComboDaoMapper.getComCodeCombo(params);
	}

	/**
	 * 제병사 생산계획 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPrdt(Map<String, Object> params) throws Exception {
		return m03070DaoMapper.searchPrdt(params);
	}

	/**
	 * 제병사 생산계획 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> savePrdt(Map<String, Object> params) throws Exception {
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

		// 추가/수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));

			m03070DaoMapper.updatePrdt(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");

		return result;
	}

	/**
	 * 용기 시뮬레이션 결과 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchResult(Map<String, Object> params) throws Exception {
		return m03070DaoMapper.searchResult(params);
	}
	
	/**
	 * 시뮬레이션
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	public Map<String, Object> simulate(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		m03070DaoMapper.simulate(params);

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "시뮬레이션이 완료되었습니다");

		return result;
	}
	
	/**
	 * 용기 시뮬레이션 결과 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveResult(Map<String, Object> params) throws Exception {
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

		// 추가/수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));

			m03070DaoMapper.updateResult(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다");

		return result;
	}
}
