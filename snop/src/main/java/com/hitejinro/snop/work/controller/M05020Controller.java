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
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M05010Service;
import com.hitejinro.snop.work.service.M05020Service;

/**
 * 프로그램 :: M05020 : 선도관리 현황
 * 작성일자 :: 2021.8.04
 * 작 성 자 :: 김태환
 */
@Controller
public class M05020Controller {

	private static final Logger logger = LoggerFactory.getLogger(M05020Controller.class);

	@Inject
	private M05020Service M05020Service;
	
	@Inject
	private M05010Service M05010Service;
	
	@Inject
	private SessionUtil sessionUtil;

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
	@RequestMapping(value = "/work/M05020", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M05020");

		//최근입력된 주차 찾기
		List<Map<String, Object>> defaultYearMonth = M05010Service.defaultYearMonth(params);
		
		if(defaultYearMonth.size() > 0) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap = defaultYearMonth.get(0);
			
			view.addObject("dYear", dataMap.get("YYYY"));
			view.addObject("dMonth", (String)dataMap.get("MM"));
			view.addObject("dWeek", dataMap.get("YYYYWW"));
		}

		return view;
	}
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M05020/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M05020/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> dateGrid = M05020Service.searchYearMonthGrid(oParams);
		
		//날짜별 column생성을 위한 다이나믹 쿼리 구문 생성
		String dynamicQueryStr = "";
		String dynamicQuerySumStr = "";
		
		for(int i=0; i<dateGrid.size(); i++) {
			Map<String, Object> gridMap = new HashMap<String, Object>();
			gridMap = dateGrid.get(i);
			
			dynamicQueryStr += ", SUM("+gridMap.get("COL_ID")+") AS "+gridMap.get("COL_ID");
			dynamicQuerySumStr += ", SUM(CASE WHEN PRODUCTION_DATE LIKE '"+gridMap.get("DINAMIC_YYYYMM")+"%' THEN T1.QUANTITY ELSE 0 END) AS "+gridMap.get("COL_ID");
		}
		
		oParams.put("dynamicQueryStr", dynamicQueryStr);
		oParams.put("dynamicQuerySumStr", dynamicQuerySumStr);
		
		List<List<Map<String, Object>>> body = M05020Service.search(oParams);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		List<Map<String, Object>> magamStr = M05010Service.searchMagamStr(oParams);

		result.put("BODY", body);
		result.put("MAGAMSTR", magamStr);
		result.put("dateGrid", dateGrid);
		result.put("RESULT", "SUCCESS");

		return result;
	}

	/**
	 * yyyyww Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M05020/yyyywwCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> yyyywwCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M05020/yyyywwCombo");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = M05020Service.yyyywwCombo(params);

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
	 * centerCode Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M05020/centerCodeCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> centerCodeCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M05020/yyyywwCombo");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = M05020Service.centerCodeCombo(params);

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
	 * 팝업 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M05020/searchPop", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchPop(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M05020/searchPop");

		Map<String, Object> result = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> body = M05020Service.searchPop(oParams);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}

}
