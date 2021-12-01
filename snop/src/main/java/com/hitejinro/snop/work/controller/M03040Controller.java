package com.hitejinro.snop.work.controller;

import java.util.HashMap;
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
import com.hitejinro.snop.work.service.M03040Service;

/**
 * 용기수급현황(월보)
 * @author 남동희
 *
 */
@Controller
public class M03040Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03040Controller.class);

	@Inject
	private M03040Service m03040Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03040
	 * @param params
	 * @return /work/M03040
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03040", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03040");

		return view;
	}

	/**
	 * 조회
	 * @param params {YYYYMM, searchType, liquorCode, brandCode, volumeValue}
	 * @return {Body : {m03040PrdtGrid : [{PERIOD_YYYYMMDD, QTY..}], m03040ReturnGrid : [{PERIOD_YYYYMMDD, QTY..}]}, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03040/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		params.put("volumeValue", ((String) params.get("volumeValue")).split(","));
		params.put("brandCode", ((String) params.get("brandCode")).split(","));

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("m03040PrdtGrid", m03040Service.searchPrdt(params));
		body.put("m03040ReturnGrid", m03040Service.searchReturn(params));

		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
}
