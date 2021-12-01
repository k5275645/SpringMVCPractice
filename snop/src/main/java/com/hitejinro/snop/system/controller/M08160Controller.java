package com.hitejinro.snop.system.controller;

import java.util.HashMap;
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
import com.hitejinro.snop.system.service.M08160Service;

/**
 * 메뉴 관리
 * @author 남동희
 *
 */
@Controller
public class M08160Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08160Controller.class);

	@Inject
	private M08160Service m08160Service;

	@Inject
	private SessionUtil sessionUtil;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * /system/M08160
	 * @param params
	 * @return /system/M08160
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08160", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		ModelAndView view = new ModelAndView("/system/M08160");
		return view;
	}

	/**
	 * 메뉴관리 조회
	 * @param params {}
	 * @return { Body : [[{MENU_CD, MENU_NM, MENU_DESC...}]], _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08160/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Const.GRID_BODY, m08160Service.search(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}

	/**
	 * 메뉴관리 저장
	 * @param params { saveData }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08160/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08160Service.save(params);

		return result;
	}
}
