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
import com.hitejinro.snop.work.service.M03052Service;

/**
 * 용기 지표 구매폐기
 * @author 남동희
 *
 */
@Controller
public class M03052Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03052Controller.class);

	@Inject
	private M03052Service m03052Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03052
	 * @param params
	 * @return /work/M03052
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03052", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03052");

		return view;
	}

	/**
	 * 조회
	 * @param params {startYYYYMMDD, endYYYYMMDD, liquorCode, vesselCode, volumeValue, vesselBrand } 
	 * @return {Body : []}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03052/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		params.put("mfgCode", ((String) params.get("mfgCode")).split(","));
		params.put("volumeValue", ((String) params.get("volumeValue")).split(","));
		params.put("brandCode", ((String) params.get("brandCode")).split(","));

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m03052Service.search(params);

		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
}
