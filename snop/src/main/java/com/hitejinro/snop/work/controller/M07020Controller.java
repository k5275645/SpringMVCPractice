package com.hitejinro.snop.work.controller;

import java.util.ArrayList;
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
import com.hitejinro.snop.work.service.M07020Service;

/**
 * 프로그램 :: M07020 :  주간 판매계획 준수율
 * 작성일자 :: 2021.09.07
 * 작 성 자 :: 김태환
 */
@Controller
public class M07020Controller {

	private static final Logger logger = LoggerFactory.getLogger(M07020Controller.class);

	@Inject
	private M07020Service M07020Service;
	
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
	@RequestMapping(value = "/work/M07020", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M07020");

		return view;
	}
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07020/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M07020/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		// 메인 그리드 조회
		List<List<Map<String, Object>>> body = M07020Service.search(oParams);
		
		// 죄종 갱신일자 조회
		List<Map<String, Object>> lastUpdtm = M07020Service.searchUpdtmStr(oParams);
		
		// 하단 상세 그리드 조회용
		List<List<Map<String, Object>>> subBody = new ArrayList<List<Map<String, Object>>>();
		// chart 조회용
		List<Map<String, Object>> chartBody = new ArrayList<Map<String, Object>>();
		// 주차별 chart 조회용
		List<Map<String, Object>> weekChartBody = new ArrayList<Map<String, Object>>();
		
		// 하단 상세 그리드 타이틀용
		String subGridTitle = "";
		String orgType = "";
		String orgCode = "";
		String orgDesc = "";
		String periodYearMonth = "";
		String periodDesc = "";
		String partCode = "";
		String periodWeek = "";
		String businessCode = "";
		
		

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}
		
		// - 메인그리드의 조회결과가 존재할 경우 최초 chart와 하단 상세 그리드를 조회한다.(조회조건 조회구분, 조직코드, 기준년월은 최상단 데이터를 기준으로 조회)
		if(body.get(0).size() > 0) {
			Map<String, Object> firstRow = body.get(0).get(0);
			
			orgType = (String) firstRow.get("ORG_TYPE");
			orgCode = (String) firstRow.get("ORG_CODE");
			orgDesc = (String) firstRow.get("ORG_DESC");
			periodYearMonth = (String) firstRow.get("PERIOD_YYYYMM");
			periodDesc = (String) firstRow.get("PERIOD_YYYYMM_DESC");
			partCode = (String) firstRow.get("PART_CODE");
			periodWeek = (String) firstRow.get("PERIOD_YYYYWW");
			businessCode = (String) firstRow.get("BUSINESS_CODE");
			
			oParams.put("orgType", orgType);
			oParams.put("orgCode", orgCode);
			oParams.put("orgDesc", orgDesc);
			oParams.put("periodYearMonth", periodYearMonth);
			oParams.put("periodDesc", periodDesc);
			oParams.put("partCode", partCode);
			oParams.put("periodWeek", periodWeek);
			
			// 하단 상세 그리드 서브 타이틀 생성
			subGridTitle += periodDesc + " [" + orgDesc + "] 품목별 상세";
			
			//하단 상세 그리드 조회
			subBody = M07020Service.searchSubGrid(oParams);
			
			//Chart 조회
			chartBody = M07020Service.searchChart(oParams);
			
			//주차별 Chart 조회
			weekChartBody = M07020Service.searchWeekChart(oParams);
			
		}

		result.put("BODY", body);
		result.put("orgDesc", orgDesc);
		result.put("UPDTM", lastUpdtm);
		result.put("SUBBODY", subBody);
		result.put("CHARTBODY", chartBody);
		result.put("WEEKCHARTBODY", weekChartBody);
		result.put("subGridTitle", subGridTitle);
		result.put("RESULT", "SUCCESS");

		return result;
	}

	/**
	 * 상세 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07020/searchDetail", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchDetail(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M07020/searchDetail");

		Map<String, Object> result = new HashMap<String, Object>();
		
		// 하단 상세 그리드 조회용
		List<List<Map<String, Object>>> subBody = M07020Service.searchSubGrid(oParams);
		// chart 조회용
		List<Map<String, Object>> chartBody = M07020Service.searchChart(oParams);
		//주차별 Chart 조회
		List<Map<String, Object>> weekChartBody = M07020Service.searchWeekChart(oParams);
		// 하단 상세 그리드 타이틀용
		String subGridTitle = "";

		if (subBody.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}
		
		String periodDesc = (String) oParams.get("periodDesc");
		String orgDesc = (String) oParams.get("orgDesc");
		
		// 하단 상세 그리드 서브 타이틀 생성
		subGridTitle += periodDesc + " [" + orgDesc + "] 품목별 상세";
		
		result.put("SUBBODY", subBody);
		result.put("CHARTBODY", chartBody);
		result.put("WEEKCHARTBODY", weekChartBody);
		result.put("subGridTitle", subGridTitle);
		result.put("RESULT", "SUCCESS");

		return result;
	}

	/**
	 * yyyyww Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07020/yyyywwCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> yyyywwCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M07020/yyyywwCombo");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = M07020Service.yyyywwCombo(params);

		if (body.size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}

}
