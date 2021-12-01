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
import com.hitejinro.snop.work.service.M07030Service;

/**
 * 프로그램 :: M07030 : 본부 판매예측 준수율
 * 작성일자 :: 2021.09.14
 * 작 성 자 :: 김태환
 */
@Controller
public class M07030Controller {

	private static final Logger logger = LoggerFactory.getLogger(M07030Controller.class);

	@Inject
	private M07030Service M07030Service;
	
	@Resource(name="readExcel")
	ReadExcel readExcel;
	
	@Resource(name="fileUtil")
	private FileUtil fileUtil;
	
	/**
	 * UI07030
	 * @param params
	 * @return /U108110
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07030", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M07030");

		return view;
	}
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07030/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M07030/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		//주차별 column생성을 위한 기준주차 조회
		List<Map<String, Object>> dateGrid = M07030Service.searchDateGrid(oParams);
		
		//주차별 column생성을 위한 다이나믹 쿼리 구문 생성
		String dynamicQueryTimeStr = "";
		
		for(int i=0; i<dateGrid.size(); i++) {
			Map<String, Object> gridMap = new HashMap<String, Object>();
			gridMap = dateGrid.get(i);

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+gridMap.get("PERIOD_SCM_YYYYWW"));
			
			dynamicQueryTimeStr += " , SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) AS ESPN_SALE_QTY_"+gridMap.get("COL_ID")
			                    +  " , SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ACTUAL_SALE_QTY ELSE 0 END) AS ACTUAL_SALE_QTY_"+gridMap.get("COL_ID")
			                    +  " , SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.DIF_SALE_QTY ELSE 0 END) AS DIF_SALE_QTY_"+gridMap.get("COL_ID")
			                    +  " , CASE WHEN SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) = 0 OR SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ACTUAL_SALE_QTY ELSE 0 END) = 0 THEN 0"
                                +  "        WHEN (ABS(SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) - SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ACTUAL_SALE_QTY ELSE 0 END))) / SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) > 1 THEN 0"
			                    +  "        WHEN (SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ACTUAL_SALE_QTY ELSE 0 END) BETWEEN SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) AND SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) * 1.1) THEN 1"
			                    +  "        ELSE (1 - (ABS(SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) - SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ACTUAL_SALE_QTY ELSE 0 END))) / SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END))"
			                    +  "   END                                                                             AS ITEM_RATE_"+gridMap.get("COL_ID")
			                    +  " , SUM(CASE WHEN PERIOD_SCM_YYYYWW = '"+gridMap.get("PERIOD_SCM_YYYYWW")+"' THEN T2.ESPN_SALE_QTY ELSE 0 END) AS KPI_RATE_"+gridMap.get("COL_ID");
		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dynamicQueryTimeStr);
		
		oParams.put("dynamicQueryTimeStr", dynamicQueryTimeStr);
		
		// 메인 그리드 조회
		List<List<Map<String, Object>>> body = M07030Service.search(oParams);
		
		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}
		
		result.put("BODY", body);
		result.put("dateGrid", dateGrid);
		result.put("RESULT", "SUCCESS");

		return result;
	}

}
