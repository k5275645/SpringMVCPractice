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

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.service.M08101Service;

/**
 * 주단위 생산 CAPA 설정
 * @author 김남현
 *
 */
@Controller
public class M08101Controller {

	@Inject
	private M08101Service m08101Service;
	
	@Inject
	private CommonComboService commonComboService;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * /work/M08101
	 * @param params
	 * @return /work/M08101
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08101", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M08101");
		return view;
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 조회
	 * @param params { liquorCd }
	 * @return {Body : [{WEEK_WORK_DCNT_TP_CODE, LIQUOR_CODE...}], Header : [{CODE, NAME, HEADER1_DESC, HEADER1_SPAN...}], _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08101/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> header = m08101Service.searchHeader(params);

		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}

		params.put("header", header);
		List<Map<String, Object>> body = m08101Service.search(params);

		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 저장
	 * @param params { saveData }
	 * @return {_RESULT_MSG, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08101/saveMst", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08101Service.save(params);

		return result;
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 팝업 > 조회
	 * @param params {} 
	 * @return {CODE, NAME, DESCRIPTION..., _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08101/searchGroupCd", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchGroupCd(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(Const.GRID_BODY, m08101Service.searchGroupCd(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
	/**
	 * 주단위 생산 CAPA 설정 > 팝업 > 저장
	 * @param params { saveData }
	 * @return {_RESULT_MSG, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08101/saveGroupCd", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveGroupCd(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08101Service.saveGroupCd(params);

		return result;
	}
	
}
