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
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M01040Service;



/**
 * 판매진척도분석(품목)
 * @author 이수헌
 *
 */
@Controller
public class M01040Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M01040Controller.class);

	@Inject
	private M01040Service m01040Service;

	@Inject
	private CommonUtils commonUtils;
	
	@Inject
	private SessionUtil sessionUtil;

	/**
	 * M01040
	 * @param params
	 * @return /work/M01040
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01040", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		ModelAndView view = new ModelAndView("/work/M01040");
		return view;
	}

	/**
	 * 조회
	 * @param params {startDate, endDate, liquorCode, vesselCode, volumeValue, includeYN, acctType, criteria}
	 * @return [Chart : [{PERIOD_SCM_YYYYWW, AVG_RETURN_QTY, PRE_AVG_RETURN_QTY}, Body : [{{TYPE, COL0, ...}}], Header : [], _RESULT_FLAG]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01040/search", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
		
		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		mResult = m01040Service.search(params);

		return mResult;
	}
	
	/**
	 * 권역 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01040/getTerritoryCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTerritoryCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01040Service.getTerritoryCombo(params);

		if (body.size() < 1) {
			result.put("MESSAGE", "조회된 데이터가 없습니다.");
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
	@RequestMapping(value = "/work/M01040/getDepartmentCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDepartmentCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01040Service.getDepartmentCombo(params);

		if (body.size() < 1) {
			result.put("MESSAGE", "조회된 데이터가 없습니다.");
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
	@RequestMapping(value = "/work/M01040/getPartCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPartCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01040Service.getPartCombo(params);

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
	@RequestMapping(value = "/work/M01040/getSalesrepCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSalesrepCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01040Service.getSalesrepCombo(params);

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
	@RequestMapping(value = "/work/M01040/getAccountCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAccountCombo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01040Service.getAccountCombo(params);

		if (body.size() < 1) {
			result.put("MESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}else {
			result.put("BODY", body);
			result.put("RESULT", "SUCCESS");
		}

		return result;
	}

	/**
	 * 중분류 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01040/getSegment2Combo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSegment2Combo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01040Service.getSegment2Combo(params);

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
	@RequestMapping(value = "/work/M01040/getSegment3Combo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSegment3Combo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01040Service.getSegment3Combo(params);

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
