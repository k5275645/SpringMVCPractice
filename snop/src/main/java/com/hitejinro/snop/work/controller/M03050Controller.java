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
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.service.M03050Service;

/**
 * 용기 지표(용기현황 Index)
 * @author 남동희
 *
 */
@Controller
public class M03050Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03050Controller.class);

	@Inject
	private M03050Service m03050Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03050
	 * @param params
	 * @return /work/M03050
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03050", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03050");

		return view;
	}

	/**
	 * 조회
	 * @param params {startYYYYMMDD, endYYYYMMDD, liquorCode, vesselCode, volumeValue, vesselBrand } 
	 * @return {Body : [{LIQUOR_CODE, VESSEL_CODE, VOLUME_VALUE, PERIOD_YYYYMMDD, QTY ...}], Header, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03050/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		params.put("volumeValue", ((String) params.get("volumeValue")).split(","));
		params.put("brandCode", ((String) params.get("brandCode")).split(","));
		
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> header = m03050Service.searchHeader(params);

		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.put("header", header);

		List<Map<String, Object>> body = m03050Service.search(params);

		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
}
