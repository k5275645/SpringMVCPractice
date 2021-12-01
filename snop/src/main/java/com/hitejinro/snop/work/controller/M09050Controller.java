package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M09050Service;

/**
 * 생산관리 > 부적합품 관리
 * @author 김남현
 *
 */
@Controller
public class M09050Controller {

	@Inject
	private M09050Service m09050Service;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * M09050
	 * @param params
	 * @return /work/M09050
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09050", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M09050");
		return view;
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 원부자재 > 조회
	 * @param { frYYYYMM, toYYYYMM }
	 * @return { Body : [{[{ NC_NCM_DATE, NC_ORGANIZATION_NAME, NC_ITEM, NC_ITEM_DESC... }]}]}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09040/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 멀티 콤보 배열화
		params.put("mfgCd", ((String) params.get("mfgCd")).split(","));
		
		result.put(Const.GRID_BODY, m09050Service.search(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
}
