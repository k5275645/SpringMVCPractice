package com.hitejinro.snop.system.controller;

import java.util.HashMap;
import java.util.List;
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
import com.hitejinro.snop.system.service.M08150Service;

/**
 * 권한 관리
 * @author 김남현
 *
 */
@Controller
public class M08150Controller {

	@Inject
	private M08150Service m08150Service;

	@Inject
	private SessionUtil sessionUtil;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M08150
	 * @param params
	 * @return /system/M08150
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08150", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/system/M08150");
		return view;
	}

	/**
	 * 권한 조회
	 * @param params { useYN }
	 * @return { Body : [[{AUTH_CD, AUTH_NM, AUTH_DESC... }]], _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08150/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m08150Service.search(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}

	/**
	 * 권한 저장
	 * @param params { saveData }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08150/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08150Service.save(params);

		return result;
	}

	/**
	 * M08150Dtl
	 * @param params
	 * @return /system/M08150Dtl
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08150Dtl", method = RequestMethod.GET)
	public ModelAndView getManageView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/system/M08150Dtl");
		return view;
	}
	
	/**
	 * 권한콤보 조회
	 * @param params { authCd, authNm }
	 * @return [[{ AUTH_CD, AUTH_NM }]]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08150/getAuth", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getAuth(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		return m08150Service.getAuth(params);
	}

	/**
	 * 권한/메뉴 조회
	 * @param params { authCd }
	 * @return [[{MENU_CD, MENU_NM, MENU_DESC...}]]
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08150Dtl/searchDetail", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchDetail(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m08150Service.searchDetail(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}

	/**
	 * 권한/메뉴 저장
	 * @param params { saveData, authCd }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08150Dtl/saveDetail", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveDetailMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08150Service.saveDetail(params);
		
		return result;
	}

}
