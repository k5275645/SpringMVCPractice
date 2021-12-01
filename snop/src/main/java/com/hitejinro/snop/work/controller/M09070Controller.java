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
import com.hitejinro.snop.work.service.M09070Service;

/**
 * 라인별 생산효율
 * @author 남동희
 *
 */
@Controller
public class M09070Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M09070Controller.class);

	@Inject
	private M09070Service m09070Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M09070
	 * @param params
	 * @return /work/M09070
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09070", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M09070");

		return view;
	}

	/**
	 * 조회
	 * @param params {YYYYMM, startDate, endDate, liquorCode, mfgCode, segment2, segment3, brandCode, usageCode, vesselCode, volumeValue, domExpCode}
	 * @return {Body : [{MFG_CODE, DEPT_CODE, BRAND_CODE, ITEM_CODE, ...}], Header, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09070/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> header = m09070Service.searchHeader(params);

		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.put("header", header);

		List<Map<String, Object>> body = m09070Service.search(params);

		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
}
