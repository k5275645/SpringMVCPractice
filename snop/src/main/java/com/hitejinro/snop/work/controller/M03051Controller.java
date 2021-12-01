package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.work.service.M03051Service;

/**
 * 공병 생산 지표
 * @author 남동희
 *
 */
@Controller
public class M03051Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03051Controller.class);

	@Inject
	private M03051Service m03051Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03051
	 * @param params
	 * @return /work/M03051
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03051", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03051");

		return view;
	}

	/**
	 * 조회
	 * @param params {searchType, startYYYYMMDD, endYYYYMMDD, acctType, liquorCode, mfgCode, volumeValue, brandCode } 
	 * @return {Body : [[{PERIOD_YYYYMMDD, LIQUOR_CODE, ITEM_CODE, QTY...}]], Header : [{CODE, NAME}], _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03051/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		params.put("mfgCode", ((String) params.get("mfgCode")).split(","));
		params.put("volumeValue", ((String) params.get("volumeValue")).split(","));
		params.put("brandCode", ((String) params.get("brandCode")).split(","));
		
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m03051Service.search(params);

		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
}
