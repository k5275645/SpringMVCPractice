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
import com.hitejinro.snop.work.service.M04010Service;

/**
 * 센터-품목별 재고 현황
 * @author 남동희
 *
 */
@Controller
public class M04010Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M04010Controller.class);

	@Inject
	private M04010Service m04010Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03050
	 * @param params
	 * @return /work/M04010
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M04010", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M04010");

		return view;
	}

	/**
	 * 조회
	 * @param params { liquorCode, orgCode, itemCode } 
	 * @return {Body : [{ORG_CODE, ORG_DESC, ITEM_CODE, DESCRIPTION, ...}], _RESULT_FLAG, stockStd : [{CODE, NAME, ...}]}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M04010/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("orgCode", String.valueOf(params.get("orgCode")).split(","));

		List<Map<String, Object>> body = m04010Service.search(params);
		
		result.put("stockStd", m04010Service.searchStockStd(params));
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
}
