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

import com.hitejinro.snop.common.util.FileUtil;
import com.hitejinro.snop.common.util.ReadExcel;
import com.hitejinro.snop.work.service.M01010Service;

/**
 * 프로그램 :: M01010 : 실시간 판매현황(당일 판매예측)
 * 작성일자 :: 2021.06.29
 * 작 성 자 :: 김태환
 */
@Controller
public class M01010Controller {

	private static final Logger logger = LoggerFactory.getLogger(M01010Controller.class);

	@Inject
	private M01010Service m08110Service;
	
	@Resource(name="readExcel")
	ReadExcel readExcel;
	
	@Resource(name="fileUtil")
	private FileUtil fileUtil;
	
	/**
	 * UI08110
	 * @param params
	 * @return /U108110
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01010", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M01010");

		return view;
	}
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01010/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M01010/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> dateGrid = m08110Service.searchDateGrid(oParams);
		
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
				
//				dynamicQueryMagamStr += "             , CASE WHEN '"+gridMap.get("YYYYMMDD")+"' = TO_CHAR(SYSDATE, 'YYYYMMDD') THEN SCMU.FN_SOP_FORECAST_SALES_QTY(ITEM_CODE,'"+calcDayCnt+"')"
				dynamicQueryMagamStr += "             , CASE WHEN '"+gridMap.get("YYYYMMDD")+"' = TO_CHAR(SYSDATE, 'YYYYMMDD') THEN T2.FORCAST_QTY"
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
		
		List<List<Map<String, Object>>> body = m08110Service.search(oParams);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("dateGrid", dateGrid);
		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}
	
}
