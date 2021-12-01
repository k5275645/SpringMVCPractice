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

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.work.dao.M08050DaoMapper;

/**
 * 기준정보 > 정량적 단종기준
 * @author 김태환
 *
 */
@Service
public class M08050Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08050Service.class);

	@Inject
	private M08050DaoMapper m08040DaoMapper;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08040DaoMapper.search(params);
		return list;
	}

	/**
	 * 데이터 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> mResult = new HashMap<String, Object>();

		try {
			
			String sResultFlag = "S";
			String sResultMsg = "";
			
			List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
			List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();

			int iUpdateCnt = 0;
			int iDeleteCnt = 0;
			int iInsertCnt = 0;
			
			// 1. 데이터 체크
			if (saveList.size() == 0) {
				mResult.put(Const.RESULT_FLAG, "F");
				mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
				throw new Exception();
			}
			
			// 2. 저장 데이터 분리 : 삭제와 추가/수정으로 분리
			for (Map<String, Object> mRow : saveList) {
				String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));

				if (Const.ROW_STATUS_INSERT.equals(sAction)) {
					insertList.add(mRow);
				}else if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
					updateList.add(mRow);
				}else if(Const.ROW_STATUS_DELETE.equals(sAction)) {
					deleteList.add(mRow);
				} else {
					mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
                    mResult.put("ROW_INFO", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
				}
			}

			// - 별도의 Map에 담기 : 화면단에서 넘어온 것과 분리
			Map<String, Object> oSaveParam = new HashMap<String, Object>();
			oSaveParam.putAll(params);
			oSaveParam.put("updateList", updateList);
			oSaveParam.put("deleteList", deleteList);
			oSaveParam.put("insertList", insertList);
			
			// 4.1 삭제 처리
			if(deleteList.size() > 0) {
				iDeleteCnt = m08040DaoMapper.delete(oSaveParam);
			}
			// 4.2 수정 처리
			if(updateList.size() > 0) {
				iUpdateCnt = m08040DaoMapper.update(oSaveParam);
			}
			// 4.3 추가 처리
			if(insertList.size() > 0) {
				// 4.3.1 추가 처리의 경우 정합성 체크
				List<Map<String, Object>> arrValidList = m08040DaoMapper.validate(oSaveParam);
				
				if(arrValidList != null && arrValidList.size() > 0 && !"0".equals(arrValidList.get(0).get("ERR_CNT").toString())) {
					mResult.put(Const.RESULT_FLAG, "F");
					mResult.put(Const.RESULT_MSG, "정합성 체크 중 오류 발생" + "\r\n" + arrValidList.get(0).get("ERR_MSG"));
					commonUtils.debugParams(mResult);
					return mResult;
				}
				
				iInsertCnt = m08040DaoMapper.insert(oSaveParam);
			}
			
			sResultFlag = "S";
			sResultMsg = "저장 성공";
			sResultMsg += "\n" + "==========================";
			sResultMsg += "\n" + "추가 : " + iInsertCnt + " 개";
			sResultMsg += "\n" + "삭제 : " + iDeleteCnt + " 개";
			sResultMsg += "\n" + "수정 : " + iUpdateCnt + " 개";
			
			mResult.put(Const.RESULT_FLAG, sResultFlag);
            mResult.put(Const.RESULT_MSG, sResultMsg);
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);
            mResult.put("INSERT_CNT", iInsertCnt);

		} catch (Exception e) {
			throw e;
		}

		return mResult;
	}
}
