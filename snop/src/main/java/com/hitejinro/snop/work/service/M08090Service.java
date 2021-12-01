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

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.work.dao.M08090DaoMapper;

/**
 * 기준정보 > 안전재고 일수
 * @author 유기후
 *
 */
@Service
public class M08090Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08090Service.class);

    @Inject
    private M08090DaoMapper m08090DaoMapper;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 데이터 조회
     * @param params { bssYYYYMM, liquorCode, itemIgrdTypeCode, stockCalcWw, itemMapYn }
     * @return [{ YYYYMM, LIQUOR_CODE, ITEM_CODE, ITEM_NAME, CALC_ITEM_IGRD_TYPE_CODE, ITEM_IGRD_TYPE_CODE, BRAND_CODE, BRAND_NAME, BRAND_SORT_ORDER, VESSEL_CODE, VESSEL_SORT, VESSEL_SORT_ORDER, VOLUME_VALUE, USAGE_CODE, USAGE_NAME, USAGE_SORT_ORDER, DOM_EXP_CODE, DOM_EXP_FLAG, ACTUAL_1M_SALE_AVG_CONV_QTY, ACTUAL_3M_SALE_AVG_CONV_QTY, ACTUAL_6M_SALE_AVG_CONV_QTY, ACTUAL_1M_SALE_RATE, ACTUAL_3M_SALE_RATE, ACTUAL_6M_SALE_RATE, MIN_STOCK_DCNT, DLV_LEAD_DCNT, PRDT_LEAD_DCNT, SAFT_STOCK_DCNT, STRG_STOCK_DCNT, STRG_STOCK_CONV_QTY, STRG_SAFT_STOCK_DCNT, STRG_SAFT_MAX_STOCK_DCNT, AVG_STOCK_DAY, MIN_STOCK_DAY, MAX_STOCK_DAY, MIN_PRDT_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = m08090DaoMapper.search(params);
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
            List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

            int iDeleteCnt = 0;
            int iUpdateCnt = 0;

            // 1. 데이터 체크
            if (saveList.size() == 0) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. 저장 데이터 분리 : 실제로는 업데이트만 존재
            for (Map<String, Object> mRow : saveList) {
                String sAction = (mRow == null ? "" : (String)mRow.get(Const.ROW_STATUS));

                if (Const.ROW_STATUS_UPDATE.equals(sAction)) {
                    updateList.add(mRow);
                } else {
                    mResult.put(Const.RESULT_FLAG, "F");
                    mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
                    mResult.put("ROW_DATA", mRow);
                    commonUtils.debugParams(mResult);
                    return mResult;
                }
            }
            
            // - 별도의 Map에 담기 : 화면단에서 넘어온 것과 분리
            Map<String, Object> oSaveParam = new HashMap<String, Object>();
            oSaveParam.putAll(params);
            oSaveParam.put("updateList", updateList);
            
            // 3. 저장 처리
            // 3.1. 추가/수정
            if(updateList.size() > 0) {
                iUpdateCnt = m08090DaoMapper.update(oSaveParam);
                if(updateList.size() != iUpdateCnt) {
                    throw new Exception("추가/수정 중 오류 발생 : 추가/수정 대상(" + updateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
                }
            } else {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            // 3.2. 삭제 : 삭제(모든 항목이 null인 경우 삭제)
            iDeleteCnt = m08090DaoMapper.delete(oSaveParam);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
            mResult.put("DELETE_CNT", iDeleteCnt);
            mResult.put("UPDATE_CNT", iUpdateCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }

    


    /**
     * 기준년월 복사
     * @param params { copyToYYYYMM, copyFrYYYYMM, userId }
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    public Map<String, Object> copy(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        try {
            int iDeleteCnt = 0;
            int iUpdateCnt = 0;

            // 1. 데이터 체크
            if(params == null || StringUtils.isEmpty(params.get("copyToYYYYMM")) || StringUtils.isEmpty(params.get("copyFrYYYYMM"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "복사할 필수 데이터가 없습니다");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if(((String)params.get("copyFrYYYYMM")).equals((String)params.get("copyToYYYYMM"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "복사원천과 대상의 기준년월이 동일합니다.");
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 2. Validation : 복사원천(From 기준년월)의 데이터 체크
            List<Map<String, Object>> arrValidateCopyList = m08090DaoMapper.validateByCopy(params);
            if(arrValidateCopyList == null || arrValidateCopyList.size() != 1) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "복사 전 Validation 중 오류 발생 : 알 수 없는 이유로 Validation이 안되고 있음");
                commonUtils.debugParams(mResult);
                return mResult;
            } else if("Y".equals((String)arrValidateCopyList.get(0).get("ERR_YN"))) {
                mResult.put(Const.RESULT_FLAG, "F");
                mResult.put(Const.RESULT_MSG, "복사 전 Validation 중 오류 발생 : " + arrValidateCopyList.get(0).get("ERR_MSG"));
                commonUtils.debugParams(mResult);
                return mResult;
            }
            
            // 3. 복사 처리
            // 3.1. 복사 대상 삭제 : To 기준년월
            iDeleteCnt = m08090DaoMapper.deleteByCopy(params);
            // 3.2. 복사 원천 -> 복사 대상
            iUpdateCnt = m08090DaoMapper.updateByCopy(params);

            // - 최종 처리
            mResult.put(Const.RESULT_FLAG, "S");
            mResult.put(Const.RESULT_MSG, "복사가 성공하였습니다.");
            mResult.put("DELETE_CNT", iDeleteCnt);
            mResult.put("UPDATE_CNT", iUpdateCnt);

        } catch (Exception e) {
            throw e;
        }

        return mResult;
    }
}
