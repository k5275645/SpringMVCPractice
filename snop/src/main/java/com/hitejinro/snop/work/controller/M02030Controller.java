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

import com.hitejinro.snop.common.service.CommonService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M02030Service;

/**
 * 제품수급 > 연간 제품수급 Simul
 * @author 유기후
 *
 */
@Controller
public class M02030Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M02030Controller.class);

    @Inject
    private M02030Service m02030Service;

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
    @RequestMapping(value = "/work/M02030", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02030");

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
    @RequestMapping(value = "/work/M02030PrdtSet", method = RequestMethod.GET)
    public ModelAndView getViewPrdtSet(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02030PrdtSet");

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
        Map<String, Object> mVerInfo = m02030Service.selectVerInfo(params);
        view.addObject("VER_INFO", commonUtils.getMapToJsonStr(mVerInfo));
        
        return view;
    }
    
    /**
     * 화면 호출 : 버전 상세 - 판매 설정
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030SaleSet", method = RequestMethod.GET)
    public ModelAndView getViewSaleSet(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02030SaleSet");

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
        Map<String, Object> mVerInfo = m02030Service.selectVerInfo(params);
        view.addObject("VER_INFO", commonUtils.getMapToJsonStr(mVerInfo));
        
        return view;
    }
    
    /**
     * 화면 호출 : 버전 상세 - 결과 설정
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030ResultSet", method = RequestMethod.GET)
    public ModelAndView getViewResultSet(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02030ResultSet");

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
        Map<String, Object> mVerInfo = m02030Service.selectVerInfo(params);
        view.addObject("VER_INFO", commonUtils.getMapToJsonStr(mVerInfo));
        
        return view;
    }



    /**
     * 데이터 조회 : 버전 마스터 조회
     * @param params { bssYYYY, useYn }
     * @return { TREEGRID_DATA:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN, WORK_INFO_10, WORK_INFO_20 }]
        List<Map<String, Object>> arrDataList = m02030Service.search(params);
        mResult.put("TREEGRID_DATA", arrDataList);

        return mResult;
    }

    /**
     * 데이터 저장 : 버전 마스터 추가/변경 저장
     * @param params { bssYYYY, useYn, saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}], VER_INFO:{}, COL_ACCT_LIST:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02030Service.save(params);

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
    @RequestMapping(value = "/work/M02030/selectPrdtVarList", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectPrdtVarList(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ PRDT_VAR_VER_CD, PRDT_VAR_VER_NM }]
        List<Map<String, Object>> arrDataList = m02030Service.selectPrdtVarList(params);
        mResult.put("PRDT_VAR_LIST", arrDataList);

        return mResult;
    }

    /**
     * 생산변수 저장 : 생산설정의 [생산변수 설정] 팝업창
     * @param params { verCd, prdtVarVerCd }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/savePrdtVarVerCd", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePrdtVarVerCd(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02030Service.savePrdtVarVerCd(params);

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
     * @return { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }], WEEK_WORK_DCNT_TP_LIST:[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/searchPrdtSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchPrdtSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_HEADER:[{  }], TREEGRID_BODY:[{  }], WEEK_WORK_DCNT_TP_LIST:[{  }] }
        mResult = m02030Service.searchPrdtSet(params);

        return mResult;
    }

    /**
     * 생산설정의 데이터 저장 : 생산설정 화면
     * @param params { saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}], VER_INFO:{}, COL_ACCT_LIST:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/savePrdtSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePrdtSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02030Service.savePrdtSet(params);

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
    @RequestMapping(value = "/work/M02030/refreshPrdtSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> refreshPrdtSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 처리
            mResult = m02030Service.refreshPrdtSet(params);

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
     * @param params { verCd, liquorCode }
     * @return { TREEGRID_DATA:[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/searchSaleSet", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchSaleSet(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_DATA:[{  }] }
        List<Map<String, Object>> arrDataList = m02030Service.searchSaleSet(params);
        mResult.put("TREEGRID_DATA", arrDataList);

        return mResult;
    }
    

    /**
     * 판매설정 - 판매변수 조회 : 판매설정의 [판매변수] 팝업창
     * @param params { verCd }
     * @return { TREEGRID_DATA:[{}], SALE_VAR_LIST:[{}], PERIOD_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/searchSaleVar", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchSaleVar(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_DATA:[{  }] }
        List<Map<String, Object>> arrDataList = m02030Service.searchSaleVar(params);
        mResult.put("TREEGRID_DATA", arrDataList);
        
        // - 추가 데이터 조회해서 담기 : 판매변수, 기간리스트(년월+연주차)
        mResult.put("SALE_VAR_LIST", commonService.getSaleVarDfntList(params)); // - [{ CODE, NAME, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, VLD_STR_DT, VLD_END_DT, SALE_VAR_TYPE, SALE_VAR_TYPE_DESC, SALE_VAR_NAME, LIQUOR_CODE, LIQUOR_DESC, USAGE_CODE, USAGE_NAME, ITEM_CODE, ITEM_NAME, VAR_VAL, CALC_STR_DT, CALC_END_DT }]
        mResult.put("PERIOD_LIST", m02030Service.selectPeriodList(params)); // - [{ CODE, NAME, VER_CD, YYYYMM, SCM_YYYYWW }]
        

        return mResult;
    }

    /**
     * 판매설정 - 판매변수 저장 : 판매설정의 [판매변수] 팝업창
     * @param params { saveData:[{}], changeRowInfoList:[{}], VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/saveSaleVar", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveSaleVar(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02030Service.saveSaleVar(params);

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
    @RequestMapping(value = "/work/M02030/calculateSale", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> calculateSale(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02030Service.calculateSale(params);

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
     * @return { TREEGRID_DATA:[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/searchResult", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchResult(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : { TREEGRID_DATA:[{  }] }
        List<Map<String, Object>> arrDataList = m02030Service.searchResult(params);
        mResult.put("TREEGRID_DATA", arrDataList);

        return mResult;
    }

    /**
     * 결과설정 - 계산
     * @param params { verCd, liquorCode, VER_INFO:{} }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02030/calculateResult", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> calculateResult(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02030Service.calculateResult(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "시뮬레이션 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    
}
