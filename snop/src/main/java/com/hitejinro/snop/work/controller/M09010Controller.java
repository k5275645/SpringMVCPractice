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
import com.hitejinro.snop.work.service.M09010Service;

/**
 * 생산계획 진척률
 * @author 남동희
 *
 */
@Controller
public class M09010Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M09010Controller.class);

	@Inject
	private M09010Service m09010Service;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * 화면 호출
	 * @param params
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09010", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		ModelAndView view = new ModelAndView("/work/M09010");
		return view;
	}

	/**
	 * 조회
	 * @param params { startDate, endDate, liquorCode, mfgCode, acctType, unit, segment2, segment3, brandCode, usageCode, vesselCode, volumeValue, domExpCode, mainFlag, eventItemFlag }
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09010/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> header = m09010Service.searchHeader(params);
		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}

		params.put("header", header);

		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, m09010Service.searchGrid(params));
		result.put("WeeklyChart", m09010Service.searchWeeklyChart(params));
		result.put("MonthlyChart", m09010Service.searchMonthlyChart(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}

}
