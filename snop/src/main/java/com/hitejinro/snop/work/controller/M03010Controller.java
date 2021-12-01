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
import com.hitejinro.snop.work.service.M03010Service;

/**
 * 당일 용기회수량 예측
 * @author 남동희
 *
 */
@Controller
public class M03010Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03010Controller.class);

	@Inject
	private M03010Service m03010Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03010
	 * @param params
	 * @return /work/M03010
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03010", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03010");

		return view;
	}

	/**
	 * 조회
	 * @param params {startDate, endDate, liquorCode, vesselCode, volumeValue, includeYN, acctType, criteria}
	 * @return [Chart : [{PERIOD_SCM_YYYYWW, AVG_RETURN_QTY, PRE_AVG_RETURN_QTY}, Body : [{{TYPE, COL0, ...}}], Header : [], _RESULT_FLAG]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03010/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		params.put("volumeValue", ((String) params.get("volumeValue")).split(","));

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> period = m03010Service.searchPeriod(params);
		if (period == null || period.isEmpty()) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.putAll(period);
		
		List<Map<String, Object>> header = m03010Service.searchHeader(params);
		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.put("header", header);
		
		result.put("Chart", m03010Service.searchChart(params));
		result.put(Const.ATTR, params.get("LAST_SYNC_TIME"));
		result.put(Const.GRID_BODY, m03010Service.search(params));
		result.put(Const.GRID_HEADER, header);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
}
