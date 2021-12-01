package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M08090Service;

/**
 * 기준정보 > 안전재고 일수
 * @author 유기후
 *
 */
@Controller
public class M08090Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08090Controller.class);

    @Inject
    private M08090Service m08090Service;

    @Inject
    private CommonComboService commonComboService;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 화면 호출
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08090", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M08090");
        return view;
    }

    /**
     * 데이터 조회
     * @param params { bssYYYYMM, liquorCode, itemIgrdTypeCode, stockCalcWw, itemMapYn }
     * @return { TREEGRID_DATA:[{}], ITEM_IGRD_TYPE_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08090/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ YYYYMM, LIQUOR_CODE, ITEM_CODE, ITEM_NAME, CALC_ITEM_IGRD_TYPE_CODE, ITEM_IGRD_TYPE_CODE, BRAND_CODE, BRAND_NAME, BRAND_SORT_ORDER, VESSEL_CODE, VESSEL_SORT, VESSEL_SORT_ORDER, VOLUME_VALUE, USAGE_CODE, USAGE_NAME, USAGE_SORT_ORDER, DOM_EXP_CODE, DOM_EXP_FLAG, ACTUAL_1M_SALE_AVG_CONV_QTY, ACTUAL_3M_SALE_AVG_CONV_QTY, ACTUAL_6M_SALE_AVG_CONV_QTY, ACTUAL_1M_SALE_RATE, ACTUAL_3M_SALE_RATE, ACTUAL_6M_SALE_RATE, MIN_STOCK_DCNT, DLV_LEAD_DCNT, PRDT_LEAD_DCNT, SAFT_STOCK_DCNT, STRG_STOCK_DCNT, STRG_STOCK_CONV_QTY, STRG_SAFT_STOCK_DCNT, STRG_SAFT_MAX_STOCK_DCNT, AVG_STOCK_DAY, MIN_STOCK_DAY, MAX_STOCK_DAY, MIN_PRDT_QTY }]
        List<Map<String, Object>> arrDataList = m08090Service.search(params);
        mResult.put("TREEGRID_DATA", arrDataList);
        
        // - 추가 데이터 조회해서 담기 : 품목구분(제품비중유형) 리스트
        Map<String, Object> mItemIgrdTypeParam = new HashMap<String, Object>();
        mItemIgrdTypeParam.put("groupCode", "ITEM_IGRD_TYPE_LIST");
        mItemIgrdTypeParam.put("useYn", "Y");
        mResult.put("ITEM_IGRD_TYPE_LIST", commonComboService.getComCodeCombo(mItemIgrdTypeParam)); // - 공통그룹코드=ITEM_IGRD_TYPE_LIST(제품비중유형) : [{ CODE, NAME }]

        return mResult;
    }

    /**
     * 데이터 저장
     * @param params { liquorCode, saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08090/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08090Service.save(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }

    /**
     * 기준년월 복사
     * @param params { copyToYYYYMM, copyFrYYYYMM }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08090/copy", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> copy(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08090Service.copy(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "복사 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }

    
}
