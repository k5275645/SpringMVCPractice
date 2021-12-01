package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.hitejinro.snop.common.util.FileUtil;
import com.hitejinro.snop.common.util.ReadExcel;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M01020Service;

/**
 * 프로그램 :: M01020 : 일일 판매현황 분석
 * 작성일자 :: 2021.7.13
 * 작 성 자 :: 김태환
 */
@Controller
public class M01020Controller {

	private static final Logger logger = LoggerFactory.getLogger(M01020Controller.class);

	@Inject
	private M01020Service m01020Service;
	
    @Inject
    private CommonService commonService;

    @Inject
    private CommonComboService commonComboService;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private CommonUtils commonUtils;

	@Resource(name="readExcel")
	ReadExcel readExcel;
	
	@Resource(name="fileUtil")
	private FileUtil fileUtil;
	
	/**
	 * UI01020
	 * @param params
	 * @return /U101020
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01020", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M01020");

        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        Map<String, Object> mTodayInfo = commonService.getTodayInfo(new HashMap<String, Object>());
        mSearchOption.put("bssYYYYMMDD", mTodayInfo.get("BF_YYYYMMDD")); // - 기준일자 : 영업일 기준 전일자
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));
        
        // - 현재일자 기준 진척률 조회
        List<Map<String, Object>> compRateData = m01020Service.searchDayMagamCnt(mSearchOption);
        if(compRateData.size() > 0) {
        	Map<String, Object> resultData = compRateData.get(0);
            view.addObject("nowDay", resultData.get("NOW_DAY"));
            view.addObject("compRate", resultData.get("COMP_RATE"));
            view.addObject("totalDayCnt", resultData.get("TOTAL_DAY_CNT"));
            view.addObject("magamDayCnt", resultData.get("MAGAM_DAY_CNT"));
        }
        
        // - 화면에 데이터 설정 : 팝업창의 그리드 콤보박스 데이터(브랜드, 용도, 용량, 용기). json string형식으로 변환 필요.
        List<Map<String, Object>> arrBrandList = commonComboService.getBrandComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        List<Map<String, Object>> arrUsageList = commonComboService.getUsageComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        List<Map<String, Object>> arrVesselList = commonComboService.getVesselComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        List<Map<String, Object>> arrVolumeList = commonComboService.getVolumeComboList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
//        List<Map<String, Object>> arrStdSaleDfnList = m01020Service.getStdSaleDfnList(new HashMap<String, Object>()); // - [{ CODE, NAME }]
        Map<String, Object> mComboData = new HashMap<String, Object>();
        mComboData.put("BRAND_LIST", arrBrandList);
        mComboData.put("USAGE_LIST", arrUsageList);
        mComboData.put("VESSEL_LIST", arrVesselList);
        mComboData.put("VOLUME_LIST", arrVolumeList);
//        mComboData.put("DFNT_LIST", arrStdSaleDfnList);
        view.addObject("GRID_COMBO_LIST", commonUtils.getMapToJsonStr(mComboData));
        
		return view;
	}
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01020/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M01020/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> inpDgrGrid = m01020Service.searchEspnSaleInpDgr(oParams);

		// - 현재일자 기준 진척률 조회
        List<Map<String, Object>> compRateData = m01020Service.searchDayMagamCnt(oParams);
		
		/*
		//차수별 column생성을 위한 다이나믹 쿼리 구문 생성
		String dynamicQueryResultSelect = "";
		String dynamicQuerySubSelect = "";
		String dynamicQueryTotalSelect = "";
		
		for(int i=0; i<inpDgrGrid.size(); i++) {
			Map<String, Object> gridMap = new HashMap<String, Object>();
			gridMap = inpDgrGrid.get(i);
		}
		
		/*
		
		//날짜별 column생성을 위한 다이나믹 쿼리 구문 생성
		String dynamicQueryTimeStr = "";
		String dynamicQueryMagamStr = "";
		String dynamicQueryMagamSumStr = "";
		
		//에상 산출기간 미 입력시 20일로 디폴트 설정
		String calcDayCnt = (String) oParams.get("calcDayCnt");
		if(calcDayCnt == null || calcDayCnt == "") {
			calcDayCnt = "20";
		}
		
		oParams.put("startYYYYMMDD", ((String)oParams.get("startYYYYMMDD")).replaceAll("-",  ""));
		oParams.put("endYYYYMMDD", ((String)oParams.get("endYYYYMMDD")).replaceAll("-",  ""));
		
		
		for(int i=0; i<dateGrid.size(); i++) {
			Map<String, Object> gridMap = new HashMap<String, Object>();
			gridMap = dateGrid.get(i);
			
			//마지막 컬럼의 경우 마감데이타의 예상을 위해 쿼리문을 구분한다.
			if(i==(dateGrid.size()-1)) {
				dynamicQueryTimeStr += ", SUM(DECODE(SALES_DATE, '"+gridMap.get("YYYYMMDD")+"', SALES_QTY, 0)) AS DAY_END";
				
				dynamicQueryMagamSumStr += ", SUM(DAY_END) AS DAY_END";
				
				dynamicQueryMagamStr += "             , CASE WHEN '"+gridMap.get("YYYYMMDD")+"' = TO_CHAR(SYSDATE, 'YYYYMMDD') THEN SCMU.FN_SOP_FORECAST_SALES_QTY(ITEM_CODE,'"+calcDayCnt+"')"
									  + "                    ELSE SUM(DECODE(SALES_DATE, '"+gridMap.get("YYYYMMDD")+"', SALES_QTY, 0))"
									  + "               END AS DAY_END";

				dynamicQueryMagamSumStr += "             , CASE WHEN '"+gridMap.get("YYYYMMDD")+"' = TO_CHAR(SYSDATE, 'YYYYMMDD') THEN '1'"
									     + "                    ELSE '0'"
									     + "               END AS DAY_END_FLAG";
				dynamicQueryTimeStr += ", '0' AS DAY_END_FLAG";
			}else {
				dynamicQueryTimeStr += ", SUM(DECODE(SALES_DATE, '"+gridMap.get("YYYYMMDD")+"', SALES_QTY, 0)) AS "+gridMap.get("COL_ID");

				dynamicQueryMagamSumStr += ", SUM("+gridMap.get("COL_ID")+") AS "+gridMap.get("COL_ID");
				
				dynamicQueryMagamStr += ", SUM(DECODE(SALES_DATE, '"+gridMap.get("YYYYMMDD")+"', SALES_QTY, 0)) AS "+gridMap.get("COL_ID");
			}
		}
		
		oParams.put("dynamicQueryTimeStr", dynamicQueryTimeStr);
		oParams.put("dynamicQueryMagamStr", dynamicQueryMagamStr);
		oParams.put("dynamicQueryMagamSumStr", dynamicQueryMagamSumStr);
		*/
		
		List<List<Map<String, Object>>> body = m01020Service.search(oParams);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("inpDgrGrid", inpDgrGrid);
		result.put("BODY", body);
		result.put("RATE_DATA", compRateData);
		result.put("RESULT", "SUCCESS");

		return result;
	}
	
	/**
	 * StdSaleDfnt Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01020/stdSaleDfntCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> stdSaleDfnt(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M01020/stdSaleDfnt");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01020Service.getStdSaleDfnList(params);

		if (body.size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}

    /**
     * 데이터 조회 : 판매현황 관리 그리드
     * @param params {  }
     * @return { TREEGRID_Sale_CURST_MNG:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M01020/searchSaleCurstMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchSaleCurstMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult.put("TREEGRID_SALE_CURST_MNG", m01020Service.searchSaleCurstMng(params));
        
        return mResult;
    }

    /**
     * 데이터 저장 : 판매현황 관리 그리드
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M01020/saveSaleCurstMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveSaleCurstMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m01020Service.saveSaleCurstMng(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    
    /**
     * 데이터 조회 : 판매량 항목 관리 그리드
     * @param params {  }
     * @return { TREEGRID_Sale_CURST_MNG:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M01020/searchStdSaleDfntMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchStdSaleDfntMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 판매량 항목관리 데이터 조회
        mResult.put("GRID_DATA_1", m01020Service.searchStdSaleDfntMng1(params));
        // - 차감 항목관리 데이터 조회
        mResult.put("GRID_DATA_2", m01020Service.searchStdSaleDfntMng2(params));
        
        return mResult;
    }

    /**
     * 데이터 저장 : 판매량 항목 관리 그리드
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M01020/saveStdSaleDfntMng", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveStdSaleDfntMng(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m01020Service.saveStdSaleDfntMng(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    

}
