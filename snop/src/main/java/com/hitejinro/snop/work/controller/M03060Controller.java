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
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.security.vo.User;
import com.hitejinro.snop.work.service.M03060Service;

/**
 * 제병사실적 관리(입력)
 * @author 남동희
 *
 */
@Controller
public class M03060Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03060Controller.class);

	@Inject
	private M03060Service m03060Service;

	@Inject
	private SessionUtil sessionUtil;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03060
	 * @param params
	 * @return /work/M03060
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03060", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03060");

		return view;
	}

	/**
	 * 조회
	 * @param params {startDate, endDate, botlManursCode, liquorCode, itemCode}
	 * @return {Body : [{PERIOD_YYYYMMDD, BOTL_MANURS_CODE, LIQUOR_CODE, ITEM_CODE, BOTL_PACKING_TYPE, ...}], Attr : [{CODE, NAME], _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03060/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m03060Service.search(params);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.ATTR, m03060Service.searchVisibleHeader(params));

		return result;
	}

	/**
	 * 저장
	 * @param params {uid, saveData : [{action, PERIOD_YYYYMMDD, BOTL_MANURS_CODE, LIQUOR_CODE, ITEM_CODE, BOTL_PACKING_TYPE, ...}]
	 * @return {_RESULT_MSG, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03060/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03060Service.save(params);

		return result;
	}
}
