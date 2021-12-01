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
import com.hitejinro.snop.work.dao.M08120DaoMapper;

/**
 * 기준정보 > 기준판매정의
 * @author 김태환
 *
 */
@Service
public class M08120Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08120Service.class);

	@Inject
	private M08120DaoMapper m08040DaoMapper;

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
			
			List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
			List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

			int iDeleteCnt = 0;
			int iUpdateCnt = 0;
			
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

				if (Const.ROW_STATUS_INSERT.equals(sAction) || Const.ROW_STATUS_UPDATE.equals(sAction)) {
					updateList.add(mRow);
                } else if (Const.ROW_STATUS_DELETE.equals(sAction)) {
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
            oSaveParam.put("deleteList", deleteList);
			oSaveParam.put("updateList", updateList);
			
			/*
			// // 3. 정합성 체크
			List<Map<String, Object>> arrValidList = m08040DaoMapper.validate(oSaveParam);
			
			if(arrValidList != null && arrValidList.size() > 0 && !"0".equals(arrValidList.get(0).get("ERR_CNT").toString())) {
				mResult.put(Const.RESULT_FLAG, "F");
				mResult.put(Const.RESULT_MSG, "정합성 체크 중 오류 발생" + "\r\n" + arrValidList.get(0).get("ERR_MSG"));
				commonUtils.debugParams(mResult);
				return mResult;
			}
			*/	

			// 4. 저장 처리
            // 4.1. 삭제
            if(deleteList.size() > 0) {
                iDeleteCnt = m08040DaoMapper.delete(oSaveParam);
                if(deleteList.size() != iDeleteCnt) {
                	throw new Exception("삭제 중 오류 발생 : 삭제할 대상(" + deleteList.size() + "건)과 처리된 대상(" + iDeleteCnt + "건) 건수가 다릅니다");
                }
            }            

            // 4.2. 추가/수정
			if (updateList.size() > 0) {
				iUpdateCnt = m08040DaoMapper.update(oSaveParam);
				
				// 해당 화면은 경우에 따라서, 그리드 데이터 1건 당 수정되는 테이블 데이터의 row 갯수가 1건이 될 수도 있고, 2건이 될 수도 있어서 처리된 대상 건수로 검증하는 것은 적절하지 않습니다. 
				//throw new Exception("추가/수정 중 오류 발생 : 추가/수정 대상(" + updateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
			}
			
			mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("UPDATE_CNT", iUpdateCnt);

		} catch (Exception e) {
			throw e;
		}

		return mResult;
	}
}
