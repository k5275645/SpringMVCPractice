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
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M03071Service;

/**
 * 용기 시뮬레이션 버전 비교
 * @author 남동희
 *
 */
@Controller
public class M03071Controller {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03071Controller.class);

	@Inject
	private M03071Service m03071Service;

	@SuppressWarnings("unused")
	@Inject
	private SessionUtil sessionUtil;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03071
	 * @param params
	 * @return /work/M03071
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03071", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03071");

		return view;
	}
	
	@RequestMapping(value = "/work/M03071/searchVerCd", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> searchVerCd(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		return m03071Service.searchVerCd(params);
	}

	/**
	 * 조회
	 * @param params {actualVerCd, expectedVerCd}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03071/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m03071Service.search(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
}
