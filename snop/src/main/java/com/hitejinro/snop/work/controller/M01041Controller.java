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

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.work.service.M01041Service;



/**
 * 판매진척도분석(조직별)
 * @author 이수헌
 *
 */
@Controller
public class M01041Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M01041Controller.class);

	@Inject
	private M01041Service m01041Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M01041
	 * @param params
	 * @return /work/M01041
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		ModelAndView view = new ModelAndView("/work/M01041");
		return view;
	}

	/**
	 * 조회
	 * @param params {startDate, endDate, liquorCode, vesselCode, volumeValue, includeYN, acctType, criteria}
	 * @return [Chart : [{PERIOD_SCM_YYYYWW, AVG_RETURN_QTY, PRE_AVG_RETURN_QTY}, Body : [{{TYPE, COL0, ...}}], Header : [], _RESULT_FLAG]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/search", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		mResult = m01041Service.search(params);

		return mResult;
	}
	
	/**
	 * 권역 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/getTerritoryCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTerritoryCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01041Service.getTerritoryCombo(params);

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
	 * 지점 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/getDepartmentCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDepartmentCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01041Service.getDepartmentCombo(params);

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
	 * 파트 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/getPartCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPartCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01041Service.getPartCombo(params);

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
	 * 담당 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/getSalesrepCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSalesrepCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01041Service.getSalesrepCombo(params);

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
	 * 거래처 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/getSiteCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSiteCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01041Service.getSiteCombo(params);

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
	 * 중분류 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/getSegment2Combo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSegment2Combo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01041Service.getSegment2Combo(params);

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
	 * 소분류 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01041/getSegment3Combo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSegment3Combo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01041Service.getSegment3Combo(params);

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
