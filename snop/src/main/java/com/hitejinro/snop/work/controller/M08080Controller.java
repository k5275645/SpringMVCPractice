package com.hitejinro.snop.work.controller;

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

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M08080Service;

/**
 * 기준정보 > 제품 과다/부족 기준
 * @author 김남현
 *
 */
@Controller
public class M08080Controller {

	@Inject
	private M08080Service m08080Service;
	
	@Inject
	private CommonComboService commonComboService;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * /work/M08080
	 * @param params
	 * @return /work/M08080
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08080", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M08080");
		return view;
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 조회
	 * @param params { yyyymm, liquorCd }
	 * @return { [{ITEM_IGRD_TYPE_CODE, STOCK_STATS_CODE, SEQ...}] }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08080/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m08080Service.search(params));
		
		// 제품 비중 유형 리스트
		Map<String, Object> itemIgrdTypeParam = new HashMap<String, Object>();
		itemIgrdTypeParam.put("groupCode", "ITEM_IGRD_TYPE_LIST");
		result.put("ITEM_IGRD_TYPE_LIST", commonComboService.getComCodeCombo(itemIgrdTypeParam));
		
		// 재고상태 리스트
		Map<String, Object> stockStatsParam = new HashMap<String, Object>();
		stockStatsParam.put("groupCode", "STOCK_STATS");
		result.put("STOCK_STATS_LIST", commonComboService.getComCodeCombo(stockStatsParam));
		
		// 재고상태 값 유형 리스트
		Map<String, Object> stockStatsValTpParam = new HashMap<String, Object>();
		stockStatsValTpParam.put("groupCode", "STOCK_STATS_VAL_TP");
		result.put("STOCK_STATS_VAL_TP_LIST", commonComboService.getComCodeCombo(stockStatsValTpParam));
		
		// 범위 부호 리스트
		Map<String, Object> rngSnParam = new HashMap<String, Object>();
		rngSnParam.put("groupCode", "RNG_SN");
		result.put("RNG_SN_LIST", commonComboService.getComCodeCombo(rngSnParam));
		
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 저장
	 * @param params { saveData }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08080/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		sessionUtil.setUserInfoParam(params);
		
		result = m08080Service.save(params);
		
		return result;
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 버전콤보조회
	 * @param params {}
	 * @return { [{CODE, NAME}] }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08080/getVerList", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getVerList(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("VER_LIST", m08080Service.getVerList(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 버전복사
	 * @param params { yyyymm, fromVer }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08080/versionCopy", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> versionCopy(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		sessionUtil.setUserInfoParam(params);
		
		result = m08080Service.versionCopy(params);
		
		return result;
	}
	
	/**
	 * 기준정보 > 제품 과다/부족 기준 > 팝업창 검색
	 * @param params { searchWord, yyyymm, liquorCd }
	 * @return [{ ITEM_CODE, DESCRIPTION }]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08080/searchPop", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchPop(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(Const.GRID_BODY, m08080Service.searchPop(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
}
