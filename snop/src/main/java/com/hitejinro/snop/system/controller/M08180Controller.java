package com.hitejinro.snop.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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
import com.hitejinro.snop.system.service.M08180Service;

/**
 * 공통코드 관리
 * @author 김남현
 *
 */
@Controller
public class M08180Controller {

	@Inject
	private M08180Service m08180Service;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;

	/**
	 * M08180
	 * @param params
	 * @return /system/M08180
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08180", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/system/M08180");
		view.addObject("searchWord", params.get("searchWord"));
		return view;
	}

	/**
	 * 공통그룹 조회
	 * @param params { groupCode, useYN }
	 * @return { Body : [[{GROUP_CODE, GROUP_NAME...}]], _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08180/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m08180Service.search(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
	/**
	 * 공통그룹 저장
	 * @param params { saveData }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08180/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08180Service.save(params);

		return result;
	}
	
	/**
	 * M08180Dtl
	 * @param params
	 * @return /system/M08180Dtl
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08180Dtl", method = RequestMethod.GET)
	public ModelAndView getDetailView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		ModelAndView view = new ModelAndView("/system/M08180Dtl");
		view.addObject("searchWord", params.get("searchWord"));
		view.addObject("groupCode", params.get("GROUP_CODE"));
		view.addObject("groupName", params.get("GROUP_NAME"));
		return view;
	}
	
	/**
	 * 공통코드 조회
	 * @param params { groupCode, useYN }
	 * @return { Body : [[{GROUP_CODE, CODE, NAME...}]], _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08180Dtl/searchDetail", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchDetail(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m08180Service.searchDetail(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
	/**
	 * 공통코드 저장
	 * @param params { saveData }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08180Dtl/saveDetail", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveDetailMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08180Service.saveDetail(params);

		return result;
	}

}
