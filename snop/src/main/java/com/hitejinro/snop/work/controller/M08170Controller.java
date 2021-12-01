package com.hitejinro.snop.work.controller;

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
import com.hitejinro.snop.work.service.M08170Service;

/**
 * 제품적재규모(공장, 물류센터)
 * @author 김남현
 *
 */
@Controller
public class M08170Controller {

	@Inject
	private M08170Service m08170Service;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * /work/M08170
	 * @param params
	 * @return /work/M08170
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08170", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M08170");
		return view;
	}
	
	/**
	 * 제품적재 버전리스트 콤보 조회
	 * @param params { verCd }
	 * @return [{ ORG_CAPA_VER_CD, ORG_CAPA_VER_NM, CREATION_DATE }, ]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08170/getVersion", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getVersion(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		return m08170Service.getVersion(params);
	}
	
	/**
	 * 제품적재규모(공장, 물류센터) 조회
	 * @param params { verCd, useYn }
	 * @return { Body : [[{ORG_CODE, ORG_NAME, ORG_WH_OPT_CAPA...}]], _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08170/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m08170Service.search(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 제품적재 규모(공장, 물류센터) 저장
	 * @param params { saveData, verCd }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08170/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		sessionUtil.setUserInfoParam(params);

		result = m08170Service.save(params);
		
		return result;
	}
	
	/**
	 * 제품적재규모(공장, 물류센터) 버전생성
	 * @param params { verNm, YYYYMM }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08170/addVersion", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addVersion(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		sessionUtil.setUserInfoParam(params);

		result = m08170Service.addVersion(params);
		
		return result;
	}
	
}
