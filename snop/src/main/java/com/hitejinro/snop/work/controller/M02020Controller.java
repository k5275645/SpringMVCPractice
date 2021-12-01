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
import com.hitejinro.snop.common.service.CommonService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M02020Service;

/**
 * 제품수급 > 일별 제품수급 Simul
 * @author 유기후
 *
 */
@Controller
public class M02020Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M02020Controller.class);

    @Inject
    private M02020Service m02020Service;

    @Inject
    private CommonComboService commonComboService;

    @Inject
    private CommonService commonService;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 화면 호출 : 버전 목록
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02020");

        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        if(params.get("SEARCH_OPTION") != null && !"".equals((String)params.get("SEARCH_OPTION"))) {
            try {
                mSearchOption = commonUtils.getJsonStrToMap((String)params.get("SEARCH_OPTION"));
            } catch(Exception e) {
                commonUtils.debugError(e);
            }
        }
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));
        
        return view;
    }
    
    /**
     * 화면 호출 : 버전 상세 - 생산 설정
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020PrdtSet", method = RequestMethod.GET)
    public ModelAndView getViewPrdtSet(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02020PrdtSet");

        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        if(params.get("SEARCH_OPTION") != null && !"".equals((String)params.get("SEARCH_OPTION"))) {
            try {
                mSearchOption = commonUtils.getJsonStrToMap((String)params.get("SEARCH_OPTION"));
            } catch(Exception e) {
                commonUtils.debugError(e);
            }
        }
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));

        // - 화면에 데이터 설정 : 버전정보(VER_INFO). json string형식으로 변환 필요.
        Map<String, Object> mVerInfo = m02020Service.selectVerInfo(params);
        view.addObject("VER_INFO", commonUtils.getMapToJsonStr(mVerInfo));
        
        return view;
    }
    
    /**
     * 화면 호출 : 버전 상세 - 판매 설정
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020SaleSet", method = RequestMethod.GET)
    public ModelAndView getViewSaleSet(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02020SaleSet");

        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        if(params.get("SEARCH_OPTION") != null && !"".equals((String)params.get("SEARCH_OPTION"))) {
            try {
                mSearchOption = commonUtils.getJsonStrToMap((String)params.get("SEARCH_OPTION"));
            } catch(Exception e) {
                commonUtils.debugError(e);
            }
        }
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));

        // - 화면에 데이터 설정 : 버전정보(VER_INFO). json string형식으로 변환 필요.
        Map<String, Object> mVerInfo = m02020Service.selectVerInfo(params);
        view.addObject("VER_INFO", commonUtils.getMapToJsonStr(mVerInfo));
        
        return view;
    }
    
    /**
     * 화면 호출 : 버전 상세 - 결과 설정
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020ResultSet", method = RequestMethod.GET)
    public ModelAndView getViewResultSet(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02020ResultSet");

        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        if(params.get("SEARCH_OPTION") != null && !"".equals((String)params.get("SEARCH_OPTION"))) {
            try {
                mSearchOption = commonUtils.getJsonStrToMap((String)params.get("SEARCH_OPTION"));
            } catch(Exception e) {
                commonUtils.debugError(e);
            }
        }
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));

        // - 화면에 데이터 설정 : 버전정보(VER_INFO). json string형식으로 변환 필요.
        Map<String, Object> mVerInfo = m02020Service.selectVerInfo(params);
        view.addObject("VER_INFO", commonUtils.getMapToJsonStr(mVerInfo));
        
        return view;
    }



    /**
     * 데이터 조회 : 버전 마스터 조회
     * @param params { bssYYYYMM, useYn }
     * @return { TREEGRID_DATA:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYYMM, USE_YN, VER_FR_DT, VER_TO_DT, WORK_INFO_10, WORK_INFO_20 }]
        List<Map<String, Object>> arrDataList = m02020Service.search(params);
        mResult.put("TREEGRID_DATA", arrDataList);

        return mResult;
    }

    /**
     * 데이터 저장 : 버전 마스터 추가/변경 저장
     * @param params { bssYYYYMM, useYn, saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}], VER_INFO:{}, COL_ACCT_LIST:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.save(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }



    /**
     * 생산변수 리스트 조회 : 생산설정의 [생산변수 설정] 팝업창
     * @param params {  }
     * @return { PRDT_VAR_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/selectPrdtVarList", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectPrdtVarList(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ PRDT_VAR_VER_CD, PRDT_VAR_VER_NM }]
        List<Map<String, Object>> arrDataList = m02020Service.selectPrdtVarList(params);
        mResult.put("PRDT_VAR_LIST", arrDataList);

        return mResult;
    }

    /**
     * 생산변수 저장 : 생산설정의 [생산변수 설정] 팝업창
     * @param params { verCd, prdtVarVerCd }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/savePrdtVarVerCd", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePrdtVarVerCd(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.savePrdtVarVerCd(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }


    /**
     * 생산설정의 데이터 조회 : 생산설정 화면
     * @param params { verCd, liquorCode }
     * @return { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }], WEEK_WORK_DCNT_TP_LIST:[{  }], SFT_PTRN_DTY_LIST:[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/searchPrdtSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchPrdtSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }], WEEK_WORK_DCNT_TP_LIST:[{  }], SFT_PTRN_DTY_LIST:[{  }] }
        mResult = m02020Service.searchPrdtSet(params);

        return mResult;
    }

    /**
     * 생산설정의 데이터 저장 : 생산설정 화면
     * @param params { saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}], VER_INFO:{}, COL_ACCT_LIST:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/savePrdtSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePrdtSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.savePrdtSet(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }


    /**
     * 생산설정 - 생산설정 갱신 : 생산설정의 [생산변수 재반영]
     * @param params { VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/refreshPrdtSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> refreshPrdtSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 처리
            mResult = m02020Service.refreshPrdtSet(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "생산변수 재반영 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }




    /**
     * 판매설정의 데이터 조회 : 판매설정 화면
     * @param params { verCd, liquorCode, domExpCode, vesselCode, usageCode }
     * @return { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/searchSaleSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchSaleSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }] }
        mResult = m02020Service.searchSaleSet(params);

        return mResult;
    }

    /**
     * 판매설정의 데이터 저장 : 판매 설정 화면
     * @param params { saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}], VER_INFO:{}, COL_ACCT_LIST:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/saveSaleSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveSaleSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.saveSaleSet(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    

    /**
     * 판매설정 - 사용판매량/실적반영유형 조회 : 판매설정의 [사용판매량] 팝업창
     * @param params { verCd, liquorCode }
     * @return { TREEGRID_DATA:[{}], USE_SALE_QTY_LIST:[{}], ACTUAL_RFLT_TYPE_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/searchUseSaleQtyActualRfltType", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchUseSaleQtyActualRfltType(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_DATA:[{  }] }
        List<Map<String, Object>> arrDataList = m02020Service.searchUseSaleQtyActualRfltType(params);
        mResult.put("TREEGRID_DATA", arrDataList);
        
        // - 추가 데이터 조회해서 담기 : 사용판매량(공통그룹코드=USE_SALE_QTY_LIST), 실적반영유형(공통그룹코드=ACTUAL_RFLT_TYPE_LIST)
        Map<String, Object> mUseSaleQtyListParam = new HashMap<String, Object>();
        mUseSaleQtyListParam.put("groupCode", "USE_SALE_QTY_LIST");
        mUseSaleQtyListParam.put("useYn", "Y");
        mResult.put("USE_SALE_QTY_LIST", commonComboService.getComCodeCombo(mUseSaleQtyListParam)); // - 공통그룹코드=USE_SALE_QTY_LIST : [{ CODE, NAME }]
        Map<String, Object> mActualRfltTypeListParam = new HashMap<String, Object>();
        mActualRfltTypeListParam.put("groupCode", "ACTUAL_RFLT_TYPE_LIST");
        mActualRfltTypeListParam.put("useYn", "Y");
        mResult.put("ACTUAL_RFLT_TYPE_LIST", commonComboService.getComCodeCombo(mActualRfltTypeListParam)); // - 공통그룹코드=ACTUAL_RFLT_TYPE_LIST : [{ CODE, NAME }]

        return mResult;
    }

    /**
     * 판매설정 - 사용판매량/실적반영유형 저장 : 판매설정의 [사용판매량] 팝업창
     * @param params { saveData:[{}], changeRowInfoList:[{}], VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/saveUseSaleQtyActualRfltType", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveUseSaleQtyActualRfltType(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.saveUseSaleQtyActualRfltType(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    

    /**
     * 판매설정 - 판매변수 조회 : 판매설정의 [판매변수] 팝업창
     * @param params { verCd }
     * @return { TREEGRID_DATA:[{}], SALE_VAR_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/searchSaleVar", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchSaleVar(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_DATA:[{  }] }
        List<Map<String, Object>> arrDataList = m02020Service.searchSaleVar(params);
        mResult.put("TREEGRID_DATA", arrDataList);
        
        // - 추가 데이터 조회해서 담기 : 판매변수
        mResult.put("SALE_VAR_LIST", commonService.getSaleVarDfntList(params)); // - [{ CODE, NAME, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, VLD_STR_DT, VLD_END_DT, SALE_VAR_TYPE, SALE_VAR_TYPE_DESC, SALE_VAR_NAME, LIQUOR_CODE, LIQUOR_DESC, USAGE_CODE, USAGE_NAME, ITEM_CODE, ITEM_NAME, VAR_VAL, CALC_STR_DT, CALC_END_DT }]

        return mResult;
    }

    /**
     * 판매설정 - 판매변수 저장 : 판매설정의 [판매변수] 팝업창
     * @param params { saveData:[{}], changeRowInfoList:[{}], VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/saveSaleVar", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveSaleVar(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.saveSaleVar(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    

    /**
     * 판매설정 - 요일별판매비율 조회 : 판매설정의 [요일별판매비율] 팝업창
     * @param params { verCd }
     * @return { TREEGRID_DATA:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/searchDowSaleRate", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchDowSaleRate(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_DATA:[{  }] }
        List<Map<String, Object>> arrDataList = m02020Service.searchDowSaleRate(params);
        mResult.put("TREEGRID_DATA", arrDataList);

        return mResult;
    }

    /**
     * 판매설정 - 요일별판매비율 저장 : 판매설정의 [요일별판매비율] 팝업창
     * @param params { saveData:[{}], changeRowInfoList:[{}], VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/saveDowSaleRate", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveDowSaleRate(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.saveDowSaleRate(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }


    /**
     * 판매설정 - 판매량 계산 : 판매량 설정의 [계산]
     * @param params { VER_INFO:{}, liquorCode }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/calculateSale", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> calculateSale(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.calculateSale(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "계산 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    

    

    /**
     * 결과설정의 데이터 조회 : 결과설정 화면
     * @param params { verCd, liquorCode, vesselCode }
     * @return { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/searchResultSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchResultSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }], PRDT_VAR_DTL_LIST:[{ }]  }
        mResult = m02020Service.searchResultSet(params);
        
        // - 용도(USAGE_LIST)
        mResult.put("USAGE_LIST", commonComboService.getUsageComboList(params)); // - [{ CODE, NAME }]

        return mResult;
    }

    /**
     * 결과설정의 데이터 저장 : 결과 설정 화면
     * @param params { saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}], VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/saveResultSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveResultSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.saveResultSet(params);
            
            // - 재고정보 Update : commit하고 나서 호출 예정
            m02020Service.callCalcStock(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }

    /**
     * 시뮬레이션 프로시저 호출 : SCMU.PKG_SOP_DALY_SCM_SIMUL.SP_MAIN_DALY_SCM_SIMUL_F
     * @param params { verCd, liquorCode, VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02020/callDalyScmSimul", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> callDalyScmSimul(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02020Service.callDalyScmSimul(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "시뮬레이션 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    
}
