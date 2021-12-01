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
import com.hitejinro.snop.work.service.M08220Service;

/**
 * 공 P-BOX/PALLET 관리
 * @author 남동희
 *
 */
@Controller
public class M08220Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08220Controller.class);

	@Inject
	private M08220Service m08220Service;

	@Inject
	private SessionUtil sessionUtil;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M08220
	 * @param params
	 * @return /work/M08220
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08220", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M08220");

		return view;
	}

	/**
	 * 조회
	 * @param params {startDate, endDate, orgType, orgCode}
	 * @return {Body : [{PERIOD_YYYYMMDD, ORG_CODE, ORG_NAME, ITEM_CODE : QTY}], Header : [{NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC}], _RESULT_FLAG : F}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08220/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> header = m08220Service.searchHeader(params);
		if (header == null || header.size() == 0) {
			result.put(Const.RESULT_MSG, "조회된 데이터가 없습니다.");
			result.put(Const.RESULT_FLAG, "F");
			return result;
		}
		
		params.put("header", header);
		List<Map<String, Object>> body = m08220Service.search(params);
		
		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}

	/**
	 * 저장
	 * @param params {uid, saveData : [{action, PERIOD_YYYYMMDD, ORG_CODE, ORG_NAME, ITEM_CODE : QTY}]
	 * @return {_RESULT_MSG, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08220/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("employeeNumber", user.getUsername());

		result = m08220Service.save(params);

		return result;
	}
}
