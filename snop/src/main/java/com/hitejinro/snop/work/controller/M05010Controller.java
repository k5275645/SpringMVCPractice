package com.hitejinro.snop.work.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.FileUtil;
import com.hitejinro.snop.common.util.ReadExcel;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.security.vo.User;
import com.hitejinro.snop.work.service.M05010Service;

/**
 * 프로그램 :: M05010 : 체화재고 현황
 * 작성일자 :: 2021.7.27
 * 작 성 자 :: 김태환
 */
@Controller
public class M05010Controller {

	private static final Logger logger = LoggerFactory.getLogger(M05010Controller.class);

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
	@RequestMapping(value = "/work/M05010", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M05010");
		
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
	@RequestMapping(value = "/work/M05010/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M05010/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> body = M05010Service.search(oParams);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}
		
		// - 요약표 조회 제거 요청 2021-08-17 진우석
//		List<Map<String, Object>> summaryResult = M05010Service.summaryResult(oParams);

		List<Map<String, Object>> magamStr = M05010Service.searchMagamStr(oParams);


		result.put("BODY", body);
		result.put("MAGAMSTR", magamStr);
//		result.put("SUMMARY", summaryResult);
		result.put("RESULT", "SUCCESS");

		return result;
	}

	/**
	 * yyyyww Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M05010/yyyywwCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> yyyywwCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M05010/yyyywwCombo");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = M05010Service.yyyywwCombo(params);

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
	 * deptCode Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M05010/deptCodeCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> deptCodeCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M05010/deptCodeCombo");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = M05010Service.deptCodeCombo(params);

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
	@RequestMapping(value = "/work/M05010/centerCodeCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> centerCodeCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M05010/yyyywwCombo");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = M05010Service.centerCodeCombo(params);

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
