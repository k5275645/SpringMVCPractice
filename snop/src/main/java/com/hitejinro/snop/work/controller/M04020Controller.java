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

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.service.M04020Service;

/**
 * 재고 조회 > 전사 제품 적재 현황
 * @author 김남현
 *
 */
@Controller
public class M04020Controller {

	@Inject
	private M04020Service m04020Service;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * M04020
	 * @param params
	 * @return /work/M04020
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M04020", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M04020");
		return view;
	}
	
	/**
	 * 전사 제품 적재 현황 > 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal }
	 * @return { Chart : [{ STD_PERIOD, TOTAL_QTY, QC_QTY, EXP_QTY, WH_LOAD_RATE, OPNSTOR_LOAD_RATE }],  Body : [{ ORG_CODE, ORG_NAME, ORG_WH_CAPA... }], _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M04020/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("qcExpStock", m04020Service.searchExpQcStock(params));
		
		Map<String, Object> period = m04020Service.searchPeriod(params);
		if (period == null || period.isEmpty()) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.putAll(period);
		
		result.put("Chart", m04020Service.searchChart(params));
		result.put(Const.GRID_BODY, m04020Service.search(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 전사 제품 적재 현황 > 저장
	 * @param params { saveData, yyyymmdd, unitVal, capaVal }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M04020/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		sessionUtil.setUserInfoParam(params);
		
		result = m04020Service.save(params);
		
		return result;
	}
	
}
