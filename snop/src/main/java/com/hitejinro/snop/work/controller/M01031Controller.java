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
import com.hitejinro.snop.work.service.M01031Service;



/**
 * 판매실적분석(품목)
 * @author 이수헌
 *
 */
@Controller
public class M01031Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M01031Controller.class);

	@Inject
	private M01031Service m01031Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M01031
	 * @param params
	 * @return /work/M01031
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01031", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		ModelAndView view = new ModelAndView("/work/M01031");

		return view;
	}

	/**
	 * 조회
	 * @param params {startDate, endDate, liquorCode, vesselCode, volumeValue, includeYN, acctType, criteria}
	 * @return [Chart : [{PERIOD_SCM_YYYYWW, AVG_RETURN_QTY, PRE_AVG_RETURN_QTY}, Body : [{{TYPE, COL0, ...}}], Header : [], _RESULT_FLAG]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01031/search", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		mResult = m01031Service.search(params);

		return mResult;
	}


	/**
	 * 중분류 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01031/getSegment2Combo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSegment2Combo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01031Service.getSegment2Combo(params);

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
	 * 소분류 Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M01031/getSegment3Combo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSegment3Combo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m01031Service.getSegment3Combo(params);

		if (body.size() < 1) {
			result.put("MESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}
	
	
	


}
