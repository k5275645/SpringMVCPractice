package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.service.CommonService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M02010Service;

/**
 * 제품수급 > 일일 품목별 PSI
 * @author 유기후
 *
 */
@Controller
public class M02010Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M02010Controller.class);

    @Inject
    private M02010Service m02010Service;

    @Inject
    private CommonService commonService;

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
    @RequestMapping(value = "/work/M02010", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02010");
        
        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        if(params.get("SEARCH_OPTION") != null && !"".equals((String)params.get("SEARCH_OPTION"))) {
            try {
                mSearchOption = commonUtils.getJsonStrToMap((String)params.get("SEARCH_OPTION"));
            } catch(Exception e) {
                commonUtils.debugError(e);
            }
        }
        if(StringUtils.isEmpty(mSearchOption.get("bssYYYYMMDD"))) {
            // - 기준일자의 초기값이 없을 경우 처리 : 영업일 기준 전일자
            Map<String, Object> mTodayInfo = commonService.getTodayInfo(new HashMap<String, Object>());
            mSearchOption.put("bssYYYYMMDD", mTodayInfo.get("BF_YYYYMMDD")); // - 기준일자 : 영업일 기준 전일자
        }
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));
        
        // - 화면에 데이터 설정 : 팝업창의 그리드 콤보박스 데이터(브랜드, 용도, 용량, 용기). json string형식으로 변환 필요.
        List<Map<String, Object>> arrBrandList = commonComboService.getBrandComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        List<Map<String, Object>> arrUsageList = commonComboService.getUsageComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        List<Map<String, Object>> arrVesselList = commonComboService.getVesselComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        List<Map<String, Object>> arrVolumeList = commonComboService.getVolumeComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        Map<String, Object> mComboData = new HashMap<String, Object>();
        mComboData.put("BRAND_LIST", arrBrandList);
        mComboData.put("USAGE_LIST", arrUsageList);
        mComboData.put("VESSEL_LIST", arrVesselList);
        mComboData.put("VOLUME_LIST", arrVolumeList);
        view.addObject("GRID_COMBO_LIST", commonUtils.getMapToJsonStr(mComboData));
        
        return view;
    }

    /**
     * 데이터 조회 : 품목별 수급 그리드, 재고현황 그리드(맥주/소주를 각각 조회), 공장 이벤트 그리드
     * @param params { bssYYYYMMDD, itemGubun(품목별기준 : INTEREST_ITEM(주요품목), ALL_ITEM(전체품목)), itemMapYn, domExpType, stdSale(판매기준), maxSale(최대판매), acctCd(계정구분 : ACCT_UOM_CS_QTY(환산c/s), ACCT_CS_QTY(단순c/s)) }
     * @return { TREEGRID_ITEM_SPL_DMD:[{}], TREEGRID_STOCK_CURST_10:[{}], TREEGRID_STOCK_CURST_20ITEM_SPL_DMD:[{}], TREEGRID_MFG_EVENT:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02010/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult = m02010Service.search(params);
        
        return mResult;
    }
    

    /**
     * 데이터 조회 : 재고현황 관리 그리드
     * @param params {  }
     * @return { TREEGRID_STOCK_CURST_MNG:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02010/searchStockCurstMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchStockCurstMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult.put("TREEGRID_STOCK_CURST_MNG", m02010Service.searchStockCurstMng(params));
        
        return mResult;
    }

    /**
     * 데이터 저장 : 재고현황 관리 그리드
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02010/saveStockCurstMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveStockCurstMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02010Service.saveStockCurstMng(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    

    /**
     * 데이터 조회 : 공장 이벤트 관리 그리드
     * @param params { bssYYYY }
     * @return { TREEGRID_MFG_EVENT_MNG:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02010/searchMfgEventMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchMfgEventMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult.put("TREEGRID_MFG_EVENT_MNG", m02010Service.searchMfgEventMng(params));
        
        return mResult;
    }

    /**
     * 데이터 저장 : 공장 이벤트 관리 그리드
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02010/saveMfgEventMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveMfgEventMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02010Service.saveMfgEventMng(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
}
