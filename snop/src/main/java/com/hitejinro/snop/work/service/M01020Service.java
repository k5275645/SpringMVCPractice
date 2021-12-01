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
import com.hitejinro.snop.work.dao.M01020DaoMapper;

/**
 * 프로그램 :: M01020 : 일일 판매현황 분석
 * 작성일자 :: 2021.7.13
 * 작 성 자 :: 김태환
 */
@Service
public class M01020Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M01020Service.class);

	@Inject
	private M01020DaoMapper m01020DaoMapper;

    @Inject
    private CommonUtils commonUtils;

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> search(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(m01020DaoMapper.search(params));

		return list;
	}

	/**
	 * 해당 월에 영업본부에서 입력한 판매예상량의 차수를 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchEspnSaleInpDgr(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		list = m01020DaoMapper.searchEspnSaleInpDgr(params);

		return list;
	}

	/**
	 * 용량 콤보 리스트 조회
	 * @param params
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getStdSaleDfnList(Map<String, Object> params) throws Exception {
		return m01020DaoMapper.getStdSaleDfnList(params);
	}

    /**
     * 데이터 조회 : 판매현황 관리 그리드
     * @param params {  }
     * @return [{ DAILY_SALES_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleCurstMng(Map<String, Object> params) throws Exception {
        return m01020DaoMapper.searchSaleCurstMng(params);
    }

    /**
     * 데이터 저장 : 판매현황 관리 그리드
     * @param params { userId, saveData:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> saveSaleCurstMng(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData"); // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> arrInsertList = new ArrayList<Map<String, Object>>(); // - [{ LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>(); // - [{ DAILY_SALES_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
            List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>(); // - [{ DAILY_SALES_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]

            int iInsertCnt = 0;
            int iUpdateCnt = 0;
            int iDeleteCnt = 0;

            // 1. 데이터 체크
            if(saveList == null || saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 분리 : 생성, 수정, 삭제로 분리
            for(Map<String, Object> mRow : saveList) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                mRow.put("userId", params.get("userId"));
                
                if(Const.ROW_STATUS_INSERT.equals(sAction)) {
                    arrInsertList.add(mRow);
                } else if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    arrUpdateList.add(mRow);
                } else if(Const.ROW_STATUS_DELETE.equals(sAction)) {
                    arrDeleteList.add(mRow);
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 정보가 없음).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // 3. 저장 처리
            if(arrDeleteList.size() > 0) {
                for(int i = 0 ; i < arrDeleteList.size() ; i++) {
                    iDeleteCnt += m01020DaoMapper.deleteSaleCurstMng(arrDeleteList.get(i));
                }
            }
            if(arrUpdateList.size() > 0) {
                for(int i = 0 ; i < arrUpdateList.size() ; i++) {
                    iUpdateCnt += m01020DaoMapper.updateSaleCurstMng(arrUpdateList.get(i));
                }
            }
            if(arrInsertList.size() > 0) {
                for(int i = 0 ; i < arrInsertList.size() ; i++) {
                    iInsertCnt += m01020DaoMapper.insertSaleCurstMng(arrInsertList.get(i));
                }
            }
            
            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("INSERT_CNT", iInsertCnt);
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }

    /**
     * 데이터 조회 : 판매현황 관리 그리드
     * @param params {  }
     * @return [{ STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, ORDER_SEQ }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchStdSaleDfntMng1(Map<String, Object> params) throws Exception {
        return m01020DaoMapper.searchStdSaleDfntMng1(params);
    }

    /**
     * 데이터 조회 : 판매현황 관리 그리드
     * @param params {  }
     * @return [{ STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, DIF_SEQ_1, DIF_SEQ_2 }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchStdSaleDfntMng2(Map<String, Object> params) throws Exception {
        return m01020DaoMapper.searchStdSaleDfntMng2(params);
    }

    /**
     * 데이터 저장 : 판매량 항목 관리 그리드
     * @param params { userId, saveData:[{}] }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> saveStdSaleDfntMng(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> saveList1 = (List<Map<String, Object>>) params.get("saveData1"); // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> saveList2 = (List<Map<String, Object>>) params.get("saveData2"); // - ROW_DATA : [ {id, action, 각 컬럼의 값들} ]
            List<Map<String, Object>> arrInsertList = new ArrayList<Map<String, Object>>(); // - [{ STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, ORDER_SEQ }]
            List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>(); // - [{ STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, ORDER_SEQ }]
            List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>(); // - [{ STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, ORDER_SEQ }]
            List<Map<String, Object>> arrUpdateList2 = new ArrayList<Map<String, Object>>(); // - [{ STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, DIF_SEQ_1, DIF_SEQ_2 }]

            int iInsertCnt = 0;
            int iUpdateCnt = 0;
            int iDeleteCnt = 0;
            int iUpdateCnt2 = 0;

            // 1. 데이터 체크
            if((saveList1 == null || saveList1.size() == 0) && (saveList2 == null || saveList2.size() == 0) ) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2.1. 저장 데이터 분리(기준판매항목) : 생성, 수정, 삭제로 분리
            for(Map<String, Object> mRow : saveList1) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                mRow.put("userId", params.get("userId"));
                
                if(Const.ROW_STATUS_INSERT.equals(sAction)) {
                    arrInsertList.add(mRow);
                } else if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    arrUpdateList.add(mRow);
                } else if(Const.ROW_STATUS_DELETE.equals(sAction)) {
                    arrDeleteList.add(mRow);
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(변경 정보가 없음).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // 2.2. 저장 데이터 분리(차감항목) : 수정 분리
            for(Map<String, Object> mRow : saveList2) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));
                mRow.put("userId", params.get("userId"));
                
                if(Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    arrUpdateList2.add(mRow);
                }
            }
            
            // 3. 저장 처리
            if(arrDeleteList.size() > 0) {
                for(int i = 0 ; i < arrDeleteList.size() ; i++) {
                    iDeleteCnt += m01020DaoMapper.deleteStdSaleDfntMng(arrDeleteList.get(i));
                }
            }
            if(arrUpdateList.size() > 0) {
                for(int i = 0 ; i < arrUpdateList.size() ; i++) {
                    iUpdateCnt += m01020DaoMapper.updateStdSaleDfntMng(arrUpdateList.get(i));
                }
            }
            if(arrInsertList.size() > 0) {
                for(int i = 0 ; i < arrInsertList.size() ; i++) {
                    iInsertCnt += m01020DaoMapper.insertStdSaleDfntMng(arrInsertList.get(i));
                }
            }
            if(arrUpdateList2.size() > 0) {
                for(int i = 0 ; i < arrUpdateList2.size() ; i++) {
                    iInsertCnt += m01020DaoMapper.updateStdSaleDfntMng2(arrUpdateList2.get(i));
                }
            }
            
            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("INSERT_CNT", iInsertCnt);
            mResult.put("UPDATE_CNT", iUpdateCnt);
            mResult.put("DELETE_CNT", iDeleteCnt);
            mResult.put("UPDATE_CNT_2", iUpdateCnt2);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }

    /**
     * 데이터 조회 : 현재일자 기준 진척률 조회
     * @param params {  }
     * @return [{ NOW_DAY_CNT, COMP_RATE, MAGAM_DAY_CNT, TOTAL_DAY_CNT }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchDayMagamCnt(Map<String, Object> params) throws Exception {
        return m01020DaoMapper.searchDayMagamCnt(params);
    }

}
