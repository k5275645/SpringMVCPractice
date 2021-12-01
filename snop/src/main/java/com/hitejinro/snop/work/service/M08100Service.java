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
import com.hitejinro.snop.work.dao.M08100DaoMapper;

/**
 * 생산 라인별 CAPA 설정
 * @author 김남현
 *
 */
@Service
public class M08100Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08100Service.class);

	@Inject
	private M08100DaoMapper m08100DaoMapper;
	
	/**
	 * 생산 라인별 CAPA 설정 > 조회
	 * @param params { verNm, useYn }
	 * @return [{ PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC... }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m08100DaoMapper.search(params);
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
		List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);
			row.put("userId", params.get("userId"));

			if (Const.ROW_STATUS_INSERT.equals(action)) {
				insertList.add(row);
			} else if (Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}
		
		// 추가
		for (Map<String, Object> row : insertList) {
			// 채번(YYYYMM001~)
			Map<String, Object> version = m08100DaoMapper.getNewVersion(params);
			row.put("prdtVarVerCd", version.get("NEW_VER_CD"));
			
			m08100DaoMapper.update(row);
			// 일일 할당시간 기본값 입력
			m08100DaoMapper.insertSfthr(row);
			// 최초 갱신
			m08100DaoMapper.renew(row);
			// 갱신일시 update
			m08100DaoMapper.updateRenewDate(row);
		}
		
		// 수정
		for (Map<String, Object> row : updateList) {
			m08100DaoMapper.update(row);
			
			// 사용여부 Y -> N, 하위 3개 테이블(DTL, SHT_HR, NEW_LINE) 데이터 삭제
			if ("N".equals(row.get("USE_YN"))) {
				m08100DaoMapper.deleteToVerDtl(row);
				m08100DaoMapper.deleteToVerNewLine(row);
				m08100DaoMapper.deleteToVerSftHr(row);
			}
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 갱신
	 * @param params { prdtVarVerCd, userId }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	public Map<String, Object> renew(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		if (params.get("prdtVarVerCd") == null) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		// 기존 데이터X & 새로운 데이터O -> INSERT
		// 기존 데이터O & 새로운 데이터O -> UPDATE
		m08100DaoMapper.renew(params);
		// 기존 데이터O & 새로운 데이터X -> DELETE
		m08100DaoMapper.deleteNotExistsRenew(params);
		
		// 갱신일시 UPDATE
		m08100DaoMapper.updateRenewDate(params);
		// 일일 할당시간 기본값 입력
		m08100DaoMapper.insertSfthr(params);
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "갱신이 완료되었습니다.");

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd, userId }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	public Map<String, Object> verCopy(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		if (params.get("fromPrdtVarVerCd") == null || params.get("toPrdtVarVerCd") == null) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		// 버전복사 > TO버전 DELETE
		m08100DaoMapper.deleteToVerDtl(params);
		m08100DaoMapper.deleteToVerNewLine(params);
		m08100DaoMapper.deleteToVerSftHr(params);
		// 버전복사 > FROM버전 INSERT
		m08100DaoMapper.insertFromVerDtl(params);
		m08100DaoMapper.insertFromVerNewLine(params);
		m08100DaoMapper.insertFromVerSftHr(params);
		// 버전복사 > TO버전 갱신일시 -> FROM버전 갱신일시
		m08100DaoMapper.updateRenewDate(params);
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "버전복사가 완료되었습니다.");

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 공장 콤보 조회
	 * @param params 
	 * @return	[{ CODE, NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMfgList(Map<String, Object> params) throws Exception {
		return m08100DaoMapper.getMfgList(params);
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > Header1 조회
	 * @param params { liquorCd }
	 * @return	[{ CODE, HEADER1_DECS, HEADER1_SPAN.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetailHeader1(Map<String, Object> params) throws Exception {
		return m08100DaoMapper.searchDetailHeader1(params);
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > Header2 조회
	 * @param params { liquorCd }
	 * @return	[{ CODE, HEADER1_DECS, HEADER1_SPAN.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetailHeader2(Map<String, Object> params) throws Exception {
		return m08100DaoMapper.searchDetailHeader2(params);
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 조회
	 * @param params { header1 : {}, header2 : {}, prdtVarVerCd, liquorCd, orgCd, expCd }
	 * @return	[{ PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, ITEM_CODE.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetail(Map<String, Object> params) throws Exception {
		return m08100DaoMapper.searchDetail(params);
	}
    
    /**
     * 생산 라인별 CAPA 설정 > 상세 > 제품라우팅 조회
     * @param params { liquorCd, prdtVarVerCd, orgCd, expCd, searchItem }
     * @return [{ LIQUOR_CODE, LIQUOR_DESC, ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME, ITEM_CODE, ITEM_NAME, VESSEL_CODE, VESSEL_NAME, VOLUME_VALUE, QTY_PER_HOUR, QTY_PER_MIN, ACTUAL_60D_SALE_QTY, UOM_CONVERSION_VALUE, PRDT_RANK }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectItemRouting(Map<String, Object> params) throws Exception {
        return m08100DaoMapper.selectItemRouting(params);
    }
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 저장
	 * @param params { saveData, userId }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveDetailMst(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
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
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}
		
		// 수정
		for (Map<String, Object> row : updateList) {
			m08100DaoMapper.updateDetail(row);
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 일일 할당시간관리 팝업 > 조회
	 * @param params { prdtVarVerCd }
	 * @return [{ PRDT_VAR_VER_CD, AVL_HR.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSftHr(Map<String, Object> params) throws Exception{
		return m08100DaoMapper.searchSftHr(params);
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 일일 할당시간관리 팝업 > 저장
	 * @param params { saveData, userId }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveSftHr(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);
			row.put("userId", params.get("userId"));

			if (Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}
		
		// 수정
		for (Map<String, Object> row : updateList) {
			m08100DaoMapper.updateSftHr(row);
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 조회 
	 * @param params { prdtVarVerCd }
	 * @return [{ PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchNewLine(Map<String, Object> params) throws Exception{
		return m08100DaoMapper.searchNewLine(params);
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 저장
	 * @param params { saveData, userId }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveNewLine(Map<String, Object> params) throws Exception {
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
			// 신규라인 삭제 
			m08100DaoMapper.deleteNewLine(row);
			// 디테일 삭제
			m08100DaoMapper.deleteDetailNewLine(row);
		}
		
		// 추가/수정
		for (Map<String, Object> row : updateList) {
			Map<String, Object> newLineCd = m08100DaoMapper.getNewLineCd(row);
			row.put("newLineCd", newLineCd.get("NEW_LINE_CODE"));
			
			// 신규라인 추가/수정
			m08100DaoMapper.updateNewLine(row);
			// 디테일 추가/수정
			m08100DaoMapper.updateDetailNewLine(row);
		}
		
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}
	
}
